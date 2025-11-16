package EstrutrurasDados.ListaEncadeada;

public class No<E>{
    
    private E dado;
    private No<E> anterior;
    private No<E> proximo;

    public No(E dado){
        this.dado = dado;
        proximo = null;
        anterior = null;
    }

    public E getDado(){
        return dado;
    }

    public No<E> getProximo(){
        return proximo;
    }

    public No<E> getAnterior(){
        return anterior;
    }

    public void setProximo(No<E> no){
        proximo = no;
    }

    public void setAnterior(No<E> no){
        anterior = no;
    }

    public void setDado(E dado){
        this.dado = dado;
    }
}
