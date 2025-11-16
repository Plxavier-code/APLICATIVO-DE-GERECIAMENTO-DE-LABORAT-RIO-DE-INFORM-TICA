package EstrutrurasDados.ListaEstatica;

import Excecoes.ListaCheiaException;
import Excecoes.ListaVaziaException;
import Excecoes.ParametroNaoValidoException;

public class ListaEstatica<E> implements ListaE<E>{
    
    private E[] lista;
    private int inicio;
    private int fim;
    private int tamanho;
    private int quantidade;

    public ListaEstatica(){
        this(10);
    }

    @SuppressWarnings("unchecked")
    public ListaEstatica(int tamanho){
        lista = (E[]) new Object[tamanho];
        this.tamanho = tamanho;
        inicio = 0;
        fim = -1;
        quantidade = -0;
    }

    private int next(int index){
        return (index + 1) % tamanho;
    }

    private int prior(int index){
        return (index + tamanho -1) % tamanho;
    }

    @Override
    public void add(E element) {
        if(!isFull()){
            fim = next(fim);
            lista[fim] = element;
            quantidade ++;

        }else{
            throw new ListaCheiaException();
        }
    }

    @Override
    public void clear() {
        quantidade = 0;
        inicio= 0;
        fim = -1;
    }

    @Override
    public E get(int index) {
        E retorno = null;

        if (!isEmpty()) {
            if(index >=0 && index < quantidade){
                int aux = inicio;
                aux = next(aux + index-1);

                retorno = lista[aux];
            } 
        }

        return retorno;
    }

    @Override
    public int indexOf(E element) {
        int retorno = -1;

        if(!isEmpty()){
            int aux = inicio;

            for(int i=0; i < quantidade; i++){
                if(lista[aux].equals(element)){
                    retorno = i;
                    break;
                }

                aux = next(aux);
            }
        }

        return retorno;
    }

    @Override
    public boolean isEmpty() {
        return quantidade == 0;
    }

    @Override
    public boolean isFull(){
        return quantidade == tamanho;
    }

    @Override
public E remove(int index) {
    if (isEmpty()) {
        throw new ListaVaziaException();
    }

    if (index < 0 || index >= quantidade) {
        throw new ParametroNaoValidoException("Index invalido.");
    }

    int physicalIndex = (inicio + index) % tamanho;
    E dadoAux = lista[physicalIndex];
    
    if (index < quantidade / 2) {

        for (int i = index; i > 0; i--) {
            int currentPhysical = (inicio + i) % tamanho;
            int priorPhysical = (inicio + i - 1) % tamanho;
            
            lista[currentPhysical] = lista[priorPhysical];
        }

        lista[inicio] = null; 
        inicio = next(inicio);

    } else {

        for (int i = index; i < quantidade - 1; i++) {
            int currentPhysical = (inicio + i) % tamanho;
            int nextPhysical = (inicio + i + 1) % tamanho;
           
            lista[currentPhysical] = lista[nextPhysical];
        }
        
        lista[fim] = null;
        fim = prior(fim);
    }

    quantidade--;
    
    return dadoAux;
}

    @Override
    public int size() {
        return quantidade;
    }
}
