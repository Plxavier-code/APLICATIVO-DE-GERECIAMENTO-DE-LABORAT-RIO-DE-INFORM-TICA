package Sistema;

public class Aluno extends Usuario{
    
    public Aluno(String matricula, String senha){
        super(TipoUsuario.ALUNO, matricula, senha);
    }
}
