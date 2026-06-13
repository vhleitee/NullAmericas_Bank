package repository;

import enums.TipoOperacaoBD;
import main.Main;

import java.sql.*;
import java.util.List;

import model.BD;
import model.Endereco;
import model.Funcionario;
import model.OperacaoBD;
import model.Usuario;
import model.Cliente;
import model.Conta;

public class UsuarioDAO implements OperacaoBD {
    private BD bd;
    private Usuario usuario;

    private PreparedStatement statement;
    private ResultSet resultSet;

    private String sql, msg;

    public UsuarioDAO() {
        bd = null;
        usuario = null;
    }

    public void setBd(BD bd) {
        this.bd = bd;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean localizar() {
        return false;
    }
    
    public Usuario localizarId(int id, Usuario usuarioInstanciado) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        this.usuario = usuarioInstanciado;
        
        if (this.usuario == null) {
            return null; 
        }
        
        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.usuario.setId(resultSet.getInt("id"));
                this.usuario.setNome(resultSet.getString("nome"));
                this.usuario.setDataCadastro(resultSet.getTimestamp("dataCadastro").toLocalDateTime().toLocalDate());
                
                Endereco endereco = new Endereco();
                endereco.setPais(resultSet.getString("pais"));
                endereco.setEstado(resultSet.getString("estado"));
                endereco.setCidade(resultSet.getString("cidade"));
                endereco.setRua(resultSet.getString("rua"));
                endereco.setNumero(resultSet.getInt("numero"));
                endereco.setComplemento(resultSet.getString("complemento"));
                endereco.setCep(resultSet.getString("cep"));
                
                this.usuario.setEndereco(endereco);
                
                if (this.usuario instanceof Cliente) {
                	Cliente cpf = (Cliente) this.usuario;
                    cpf.setDocumento(resultSet.getString("documento"));
                }else if (this.usuario instanceof Funcionario) {
                    Funcionario func = (Funcionario) this.usuario;
                    func.setCpf(resultSet.getString("documento"));
                }
                
                usuario.setTipoDocumento(resultSet.getString("tipoDocumento"));
                
                return this.usuario;
            } else {
                return null;
            }

        } catch (SQLException erro) {
            erro.printStackTrace(); 
            return null;
        }
    }
    
    public String atualizar(TipoOperacaoBD operacao) {
        return "teste";
    }
}