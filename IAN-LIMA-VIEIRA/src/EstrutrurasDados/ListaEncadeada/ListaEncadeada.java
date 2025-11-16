package EstrutrurasDados.ListaEncadeada;

import java.util.Iterator;
import Excecoes.ListaVaziaException;
import Excecoes.ParametroNaoValidoException;

public class ListaEncadeada<E> implements Lista<E>, Iterable<E>{
    private No<E> inicio;
    private No<E> fim;
    private int size;

    public ListaEncadeada(){
        inicio = null;
        fim = null;
        size = 0;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean isEmpty(){
        return size() == 0;
    }

    @Override
    public int indexOf(E element){
        int index = -1;

        if(!isEmpty()){
            No<E> aux = inicio;
            index = 0;

            while (aux != null) {
                if(aux.getDado().equals(element)){
                    break;
                }
                
                if(aux.getProximo() == null){
                    index = -1;
                    break;
                }

                aux = aux.getProximo();
                index ++;
            }
        }else{
            throw new ListaVaziaException();
        }
        
        return index;
    }

    @Override 
    public E get(int index){
        E element = null;

        if(!isEmpty()){
            if(index >=0 && index < size()){
                No<E> aux = inicio;

                for(int i=0; i<index; i++){
                    aux = aux.getProximo();
                }

                element = aux.getDado();
            }else{
                throw new ParametroNaoValidoException("Indice fora do limite");
            }
        }else{
            throw new ListaVaziaException();
        }

        return element;
    }

    @Override
    public void add(E element){
        No<E> aux = new No<>(element);

        if(isEmpty()){
            inicio = aux;
        }else{
            fim.setProximo(aux);
            aux.setAnterior(fim);
        }

        fim = aux;
        size++;
    }

    @Override
    public E remove(int index){
        E dado = null;

        if(!isEmpty()){
            if(index >= 0 && index < size()){
                if (index <= ((size()-1)/2)) {
                    No<E> aux = inicio;

                    for(int i=0; i<index; i++){
                        aux = aux.getProximo();
                    }

                    dado = aux.getDado();
                    No<E> anterior = aux.getAnterior();
                    No<E> proximo = aux.getProximo();

                    if(anterior != null){
                        anterior.setProximo(proximo);
                    }else{
                        inicio =inicio.getProximo();
                    }

                    if(proximo != null){
                        proximo.setAnterior(anterior);
                    }else{
                        fim = fim.getAnterior();
                    }

                }else{
                    No<E> aux = fim;
                    for(int i=0; i<size()-1-index; i++){
                        aux = aux.getAnterior();
                    }

                    dado = aux.getDado();
                    No<E> anterior = aux.getAnterior();
                    No<E> proximo = aux.getProximo();

                    if(anterior != null){
                        anterior.setProximo(proximo);
                    }else{
                        inicio =inicio.getProximo();
                    }

                    if(proximo != null){
                        proximo.setAnterior(anterior);
                    }else{
                        fim = fim.getAnterior();
                    }
                }
                size--;

            }else{
                throw new ParametroNaoValidoException("Indice invalido.");
            }
        }else{
            throw new ListaVaziaException();
        }

        return dado;
    }

    @Override
    public void clear(){
        inicio = null;
        fim = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private No<E> atual = inicio;

            @Override
            public boolean hasNext() {
                return atual != null;
            }

            @Override
            public E next() {
                E dado = atual.getDado();
                atual = atual.getProximo();
                return dado;
            }
        };
    }
}
