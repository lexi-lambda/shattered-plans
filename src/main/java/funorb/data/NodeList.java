package funorb.data;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static funorb.data.NodeList.Node;

public final class NodeList<T extends Node> extends AbstractCollection<T> {
  private final Node head;

  public NodeList() {
    this.head = new Node();
    this.head.next = this.head;
    this.head.prev = this.head;
  }

  @Override
  public boolean isEmpty() {
    return this.head == this.head.next;
  }

  @Override
  public int size() {
    int count = 0;
    Node next = this.head.next;
    while (next != this.head) {
      count++;
      next = next.next;
    }
    return count;
  }

  @Override
  public boolean add(final T node) {
    this.addLast(node);
    return true;
  }

  public void addFirst(final T newFirst) {
    newFirst.unlink();

    final Node oldFirst = this.head.next;
    this.head.next = newFirst;
    newFirst.prev = this.head;
    newFirst.next = oldFirst;
    oldFirst.prev = newFirst;
  }

  public void addLast(final T newLast) {
    newLast.unlink();

    final Node oldLast = this.head.prev;
    oldLast.next = newLast;
    newLast.prev = oldLast;
    newLast.next = this.head;
    this.head.prev = newLast;
  }

  @Override
  public void clear() {
    Node first;
    while ((first = this.head.next) != this.head) {
      first.unlink();
    }
  }

  @Override
  public @NotNull Iterator<T> iterator() {
    return new Itr();
  }

  public @NotNull Iterator<T> descendingIterator() {
    return new DescendingItr();
  }

  public static class Node {
    protected Node next;
    protected Node prev;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public final boolean isLinked() {
      return this.prev != null;
    }

    public final void unlink() {
      if (this.prev != null) {
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.prev = null;
        this.next = null;
      }
    }
  }

  private final class Itr implements Iterator<T> {
    private Node lastReturned = null;
    private Node next = NodeList.this.head.next;

    @Override
    public boolean hasNext() {
      return this.next != NodeList.this.head;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      final Node here = this.next;
      this.next = here.next;
      return (T) (this.lastReturned = here);
    }

    @Override
    public void remove() {
      if (this.lastReturned == null) {
        throw new IllegalStateException();
      }
      this.lastReturned.unlink();
      this.lastReturned = null;
    }
  }

  private final class DescendingItr implements Iterator<T> {
    private Node lastReturned = null;
    private Node next = NodeList.this.head.prev;

    @Override
    public boolean hasNext() {
      return this.next != NodeList.this.head;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      final Node here = this.next;
      this.next = here.prev;
      return (T) (this.lastReturned = here);
    }

    @Override
    public void remove() {
      if (this.lastReturned == null) {
        throw new IllegalStateException();
      }
      this.lastReturned.unlink();
      this.lastReturned = null;
    }
  }
}
