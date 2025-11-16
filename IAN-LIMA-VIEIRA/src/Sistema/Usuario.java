package Sistema;
import Excecoes.*;

public class Usuario {
    
    private String matricula;
    private String senha;
    private TipoUsuario tipo;

    public Usuario(TipoUsuario tipo, String matricula, String senha){
        validarMatricula(matricula);
        validarSenha(senha);

        this.tipo = tipo;
        this.matricula = matricula;
        this.senha = senha;
    }

    private void validarMatricula(String matricula){
        if(!matricula.matches("[0-9]+")){
            throw new FormatacaoMatriculaInvalidaException("Matricula deve conter somente valores inteiros.");
        }
    }

    private void validarSenha(String senha){
        String regra = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%&*.]).{8,}";
        if(!senha.matches(regra)){
            String erro = "Senha invalida: \n";

            if(!senha.matches(".*[A-Z].*")){
                erro +="-> Deve conter letra MAIUSCULA. \n";
            }
            if(!senha.matches(".*[0-9].*")){
                erro += "-> Deve conter numero. \n";
            }
            if(!senha.matches(".*[!@#$%&*.].*")){
                erro += "-> Deve conte caracter especial. \n";
            }
            if(!senha.matches(".{8,}")){
                erro += "-> Senha deve ter pelo menos 8 caracteres.";
            }

            throw new FormatacaoSenhaInvalidaException(erro);
        }
    }

    public String getMatricula(){
        return matricula;
    }

    public String getSenha(){
        return senha;
    }

    public TipoUsuario getTipo(){
        return tipo;
    }
}
