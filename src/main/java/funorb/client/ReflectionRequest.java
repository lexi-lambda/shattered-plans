package funorb.client;

import funorb.io.CipheredBuffer;
import funorb.shatteredplans.client.MailboxMessage;
import funorb.shatteredplans.client.MessagePumpThread;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.IntStream;

public final class ReflectionRequest {
  public static Queue<ReflectionRequest> pending = new ArrayDeque<>();

  public final int id;
  public final int messageCount;
  public final int[] types;
  public final int[] statusCodes;
  public final MailboxMessage[] fieldMessages;
  public final int[] fieldValues;
  public final MailboxMessage[] methodMessages;
  public final byte[][][] serializedParams;

  private ReflectionRequest(final int id, final int messageCount) {
    this.id = id;
    this.messageCount = messageCount;
    this.types = new int[this.messageCount];
    this.statusCodes = new int[this.messageCount];
    this.fieldMessages = new MailboxMessage[this.messageCount];
    this.fieldValues = new int[this.messageCount];
    this.methodMessages = new MailboxMessage[this.messageCount];
    this.serializedParams = new byte[this.messageCount][][];
  }

  public static void recieve(final MessagePumpThread messagePumpThread, @SuppressWarnings("SameParameterValue") final CipheredBuffer packet) {
    final int messageCount = packet.readUByte();
    final int id = packet.readInt();
    final ReflectionRequest req = new ReflectionRequest(id, messageCount);

    for (int i = 0; i < req.messageCount; ++i) {
      try {
        final int type = packet.readUByte();
        if (type == 0 || type == 1 || type == 2) {
          final String className = packet.readNullTerminatedString();
          final String fieldName = packet.readNullTerminatedString();
          final int fieldValue = type == 1 ? packet.readInt() : 0;

          req.types[i] = type;
          req.fieldValues[i] = fieldValue;
          req.fieldMessages[i] = messagePumpThread.sendGetDeclaredFieldMessage(classForName(className), fieldName);
        } else if (type == 3 || type == 4) {
          final String className = packet.readNullTerminatedString();
          final String methodName = packet.readNullTerminatedString();

          final int var9 = packet.readUByte();
          final String[] paramTypeNames = IntStream.range(0, var9)
              .mapToObj(j -> packet.readNullTerminatedString())
              .toArray(String[]::new);

          final byte[][] serializedObjects = new byte[var9][];
          if (type == 3) {
            for (int j = 0; j < var9; ++j) {
              final int objectLen = packet.readInt();
              serializedObjects[j] = new byte[objectLen];
              packet.readBytes(serializedObjects[j], objectLen);
            }
          }

          req.types[i] = type;
          final Class<?>[] paramTypes = new Class[var9];
          for (int var13 = 0; var13 < var9; ++var13) {
            paramTypes[var13] = classForName(paramTypeNames[var13]);
          }

          req.methodMessages[i] = messagePumpThread.sendGetDeclaredMethodMessage(classForName(className), methodName, paramTypes);
          req.serializedParams[i] = serializedObjects;
        }
      } catch (final ClassNotFoundException e) {
        req.statusCodes[i] = Status.CLASS_NOT_FOUND;
      } catch (final SecurityException e) {
        req.statusCodes[i] = Status.SECURITY_EXCEPTION;
      } catch (final NullPointerException e) {
        req.statusCodes[i] = Status.NULL_POINTER_EXCEPTION;
      } catch (final Exception e) {
        req.statusCodes[i] = Status.OTHER_EXCEPTION;
      } catch (final Throwable e) {
        req.statusCodes[i] = Status.OTHER_THROWABLE;
      }
    }

    pending.add(req);
  }

  private static Class<?> classForName(final String name) throws ClassNotFoundException {
    return switch (name) {
      case "B" -> Byte.TYPE;
      case "C" -> Character.TYPE;
      case "D" -> Double.TYPE;
      case "F" -> Float.TYPE;
      case "I" -> Integer.TYPE;
      case "J" -> Long.TYPE;
      case "S" -> Short.TYPE;
      case "Z" -> Boolean.TYPE;
      default -> Class.forName(name);
    };
  }

  public static final class Type {
    public static final int STATIC_FIELD_GET = 0;
    public static final int STATIC_FIELD_SET = 1;
    public static final int FIELD_GET_MODIFIERS = 2;
    public static final int METHOD_INVOKE = 3;
    public static final int METHOD_GET_MODIFIERS = 4;

  }

  @SuppressWarnings("WeakerAccess")
  public static final class Status {
    public static final int OK = 0;
    public static final int CLASS_NOT_FOUND = -1;
    public static final int SECURITY_EXCEPTION = -2;
    public static final int NULL_POINTER_EXCEPTION = -3;
    public static final int OTHER_EXCEPTION = -4;
    public static final int OTHER_THROWABLE = -5;
  }
}
