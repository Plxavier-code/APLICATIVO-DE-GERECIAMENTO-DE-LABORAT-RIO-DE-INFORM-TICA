package EstrutrurasDados.ListaEncadeada;

public interface Lista <E> {
    
    void add(E element);
    int indexOf(E element);
    E get(int index);
    E remove(int index);
    void clear();
    boolean isEmpty();
    int size();
}