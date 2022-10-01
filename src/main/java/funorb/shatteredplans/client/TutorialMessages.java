package funorb.shatteredplans.client;

import funorb.Strings;
import funorb.cache.ResourceLoader;
import funorb.shatteredplans.client.game.TutorialObjective;
import org.intellij.lang.annotations.MagicConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TutorialMessages {
  private static TutorialMessage[] tutorialMessages;

  private static String[] lines;
  private static int nextLineIndex;
  private static String currentLine;

  public static void load(final ResourceLoader var0) {
    lines = readLines(Objects.requireNonNull(var0.getResource("", "tutorial.txt")));
    if (lines.length == 0) {
      tutorialMessages = null;
    } else {
      nextLineIndex = 0;
      advanceLine();
      final List<TutorialMessage> messages = Stream.iterate(parseMessage(), Objects::nonNull, var2 -> parseMessage()).toList();

      tutorialMessages = messages.toArray(new TutorialMessage[0]);

      for (int i = 0; i < messages.size() - 1; ++i) {
        final String nextId = tutorialMessages[i].nextId;
        TutorialMessage next = nextId == null ? null : get(nextId);
        if (next == null && !tutorialMessages[i].endThread) {
          next = tutorialMessages[i + 1];
        }

        tutorialMessages[i].next = next;
      }
    }
  }

  public static TutorialMessage get(final String var0) {
    return Arrays.stream(tutorialMessages)
        .filter(var3 -> var0.equalsIgnoreCase(var3.tag))
        .findFirst().orElse(null);
  }

  private static void advanceLine() {
    if (nextLineIndex < lines.length) {
      currentLine = lines[nextLineIndex++];
    } else {
      currentLine = null;
    }
  }

  private static TutorialMessage parseMessage() {
    for (; currentLine != null; advanceLine()) {
      if (currentLine.charAt(0) == '[') {
        final TutorialMessage message = new TutorialMessage();
        final StringBuilder sb = new StringBuilder();
        advanceLine();

        for (; currentLine != null && currentLine.charAt(0) != '['; advanceLine()) {
          if (currentLine.charAt(0) == '@') {
            final int var3 = currentLine.indexOf(' ');
            final String var4;
            final String[] args;
            if (var3 == -1) {
              var4 = currentLine.substring(1);
              args = null;
            } else {
              var4 = currentLine.substring(1, var3);
              args = parseArguments(currentLine.substring(var3));
            }

            if (var4.equalsIgnoreCase("topleft")) {
              setMessagePosition(message, TutorialMessage.Anchor.TOP_LEFT, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("topright")) {
              setMessagePosition(message, TutorialMessage.Anchor.TOP_RIGHT, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("bottomleft")) {
              setMessagePosition(message, TutorialMessage.Anchor.BOTTOM_LEFT, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("bottomright")) {
              setMessagePosition(message, TutorialMessage.Anchor.BOTTOM_RIGHT, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("tag")) {
              message.tag = Objects.requireNonNull(args)[0];
            } else if (var4.equalsIgnoreCase("next")) {
              message.nextId = Objects.requireNonNull(args)[0];
            } else if (var4.equalsIgnoreCase("objective")) {
              addMessageObjective(message, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("action")) {
              setMessageAction(message, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("flag")) {
              setMessageFlag(message, Objects.requireNonNull(args));
            } else if (var4.equalsIgnoreCase("endthread")) {
              message.endThread = true;
            } else if (var4.equalsIgnoreCase("nostack")) {
              message.noStack = true;
            } else if (var4.equalsIgnoreCase("clearstack")) {
              message.clearStack = true;
            }
          } else {
            sb.append(currentLine);
            sb.append(' ');
          }
        }

        message.body = sb.toString();
        return message;
      }
    }

    return null;
  }

  private static String[] readLines(final byte[] data) {
    final List<String> lines = new ArrayList<>();
    int pos = 0;
    int lineStart = 0;
    while (pos < data.length) {
      while (pos < data.length && (data[pos] != '\n' && data[pos] != '\r')) {
        ++pos; // advance until next newline character
      }

      lines.add(Strings.decode1252String(data, lineStart, pos - lineStart));

      while (pos < data.length && (data[pos] == '\n' || data[pos] == '\r')) {
        ++pos; // advance past newline characters
      }
      lineStart = pos;
    }

    return lines.toArray(new String[0]);
  }

  private static String[] parseArguments(final String var0) {
    return Arrays.stream(var0.split(","))
        .map(String::trim)
        .filter(Predicate.not(String::isEmpty))
        .toArray(String[]::new);
  }

  private static void setMessagePosition(final TutorialMessage var2,
                                         @MagicConstant(valuesFromClass = TutorialMessage.Anchor.class) final int anchor,
                                         final String[] args) {
    var2.anchor = anchor;
    var2._t = autoOrInteger(args[0]);
    var2._b = autoOrInteger(args[1]);
    var2._j = autoOrInteger(args[2]);
    var2._q = autoOrInteger(args[3]);
  }

  private static int autoOrInteger(final String str) {
    return str.equalsIgnoreCase("auto") ? -1 : Strings.parseDecimalInteger(str);
  }

  private static void setMessageAction(final TutorialMessage message, final String[] args) {
    for (final String var5 : args) {
      message.a984(var5);
    }
  }

  private static void setMessageFlag(final TutorialMessage message, final String[] args) {
    for (final String var4 : args) {
      message.a627(var4);
    }
  }

  private static void addMessageObjective(final TutorialMessage message, final String[] args) {
    final TutorialObjective var3 = new TutorialObjective();
    var3.key = args[0];
    if (args.length > 2) {
      var3.description = Arrays.stream(args, 2, args.length)
          .collect(Collectors.joining(", ", args[1], ""));
    } else {
      var3.description = args[1];
    }
    message.addObjective(var3);
  }
}
