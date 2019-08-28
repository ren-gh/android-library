
package com.rengh.java.study.own.linkedlist;

public class SingleLinkedList {
    private int count;
    private Node head;
    private Node tail;

    public int getSize() {
        return count;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public Node getNode(int index) {
        if (index >= count || index < 0) {
            throw new IndexOutOfBoundsException("下标越界");
        }
        Node node = head;
        for (int i = 1; i <= index; i++) {
            node = node.getNext();
        }
        return node;
    }

    public void addNode(Node node) {
        if (null == head) {
            head = node;
            tail = head;
        } else {
            tail.setNext(node);
            tail = tail.getNext();
        }
        count++;
    }

    public void deleteNode(Node node) {
        if (0 == count) {
            return;
        }
        if (null == node) {
            return;
        }
        Node temp = head;
        Node priv = null;
        do {
            if (temp.getObject() != null
                    && temp.getObject().equals(node.getObject())) {
                if (null == priv) {
                    temp = temp.getNext();
                    head = temp;
                } else if (temp.getNext() == null) {
                    temp = null;
                    priv.setNext(null);
                    tail = priv;
                } else {
                    priv.setNext(temp.getNext());
                    temp = temp.getNext();
                }
                count--;
            } else {
                priv = temp;
                temp = temp.getNext();
            }
        } while (temp != null);
    }

    public void deleteNode(int index) {
        if (index < 0 || index >= count) {
            return;
        }
        Node temp = head;
        Node priv = null;
        int i = 0;
        do {
            if (i == index) {
                if (priv == null) {
                    temp = temp.getNext();
                    head = temp;
                } else if (temp.getNext() == null) {
                    temp = null;
                    priv.setNext(null);
                    tail = priv;
                } else {
                    priv.setNext(temp.getNext());
                    temp = temp.getNext();
                }
                count--;
            } else {
                priv = temp;
                temp = temp.getNext();
            }
            i++;
        } while (i <= index);
    }

    @Override
    public String toString() {
        if (null == head) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        Node tmp = head;
        while (null != tmp) {
            buffer.append(tmp.getObject());
            tmp = tmp.getNext();
            if (null != tmp) {
                buffer.append(", ");
            }
        }
        buffer.append("}");
        return buffer.toString();
    }

    public static class Node {
        private Object object;
        private Node next;

        public Node() {
        }

        public Node(Object object) {
            setObject(object);
        }

        public Node(Object object, Node next) {
            setObject(object);
            setNext(next);
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public boolean hasNext() {
            return null != next;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
        SingleLinkedList singleLinkedList = new SingleLinkedList();
        int count = 10;
        System.out.println("--->添加 " + count + " 个元素……");
        for (int i = 0; i < count; i++) {
            singleLinkedList.addNode(new Node(0));
            singleLinkedList.addNode(new Node(i));
        }
        System.out.println("--->添加完成！");

        System.out.println("--->链表节点个数：" + singleLinkedList.getSize());

        System.out.println("--->遍历链表：" + singleLinkedList.toString());

        System.out.println("--->删除值为 0 的元素");
        singleLinkedList.deleteNode(new Node(0));
        System.out.println("--->删除值第 0 个元素");
        singleLinkedList.deleteNode(0);
        System.out.println("--->遍历链表：" + singleLinkedList.toString());

        System.out.println("--->头节点：" + singleLinkedList.getHead().getObject());

        System.out.println("--->尾节点：" + singleLinkedList.getTail().getObject());

        int index = 0;
        System.out.println("--->第 " + index + " 个元素：" + singleLinkedList.getNode(index).getObject());
    }

}
