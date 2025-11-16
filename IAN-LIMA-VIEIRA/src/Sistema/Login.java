package Sistema;

import EstrutrurasDados.ListaEncadeada.ListaEncadeada;

public class Login {
    private ListaEncadeada<Usuario> listaLogin;

    public Login(){
        listaLogin = new ListaEncadeada<>();
    }

    public boolean verificarEspaco(Usuario a){
        int aux = 0;
        
        for(Usuario i : listaLogin){
            if(a.getMatricula().equals(i.getMatricula())){
                if(a.getTipo() == i.getTipo()){
                    aux++;
                }  
            }
        } 

        return aux == 0;
    }

    public Usuario logar(TipoUsuario tipo, String matricula, String senha){
        Usuario usuario = null;
        int aux = 0;

        if(listaLogin.isEmpty()){
            System.out.println("Senha ou Usuario invalido.");
        }else{
            for(Usuario a : listaLogin){
                if(matricula.equals(a.getMatricula()) && tipo.equals(a.getTipo())){
                    if(senha.equals(a.getSenha())){
                        aux++;
                        usuario = a;
                        System.out.println("Logado com sucesso.");
                        break;
                    }else{
                        aux++;
                        System.out.println("Senha ou Usuario invalido.");
                        break;
                    }
                }
            }
            
            if(aux == 0){
                System.out.println("Senha ou Usuario invalido.");
            }
        }

        return usuario;
    }
    

    public void cadastrar(TipoUsuario tipo, String matricula, String senha){
        Usuario usuario = null;

        switch (tipo) {
            case ALUNO:
                usuario = new Aluno(matricula, senha);
                break;
        
            case PROFESSOR: 

                usuario = new Professor(matricula, senha);
                break;
                
            case ADMINISTRADOR:

                usuario = new Administrador(matricula, senha);
                break;

            default:
                System.out.println("Tipo invalido");
        }

        if (verificarEspaco(usuario)) {
            listaLogin.add(usuario);
        }else{
            System.out.println("Usuario ja existe.");
        }
    }
}
