	package repository;
	
	import enums.TipoOperacaoBD;
	import java.sql.*;
	import java.util.ArrayList;
	import java.util.List;
	
	import model.BD;
	import model.OperacaoBD;
	import model.Usuario;
	import model.Cliente;
	import model.Conta;
	
	public class ContaDAO implements OperacaoBD {
		private BD bd;
		private Conta conta;
		private Usuario usuario;
	
		private PreparedStatement statement;
		private ResultSet resultSet;
	
		private String sql, msg;
	
		public ContaDAO() {
			bd = null;
			conta = null;
		}
	
		public void setBd(BD bd) {
			this.bd = bd;
		}
	
		public Conta getConta() {
			return conta;
		}
	
		public void setConta(Conta conta) {
			this.conta = conta;
		}
	
		public List<Conta> localizarContas(Usuario usuarioInstanciado) {
		    String sql = "SELECT * FROM conta WHERE idUser = ?";
		    List<Conta> listaContas = new ArrayList<>();
		    this.usuario = usuarioInstanciado;

		    try {
		        statement = bd.connection.prepareStatement(sql);
		        statement.setInt(1, this.usuario.getId());

		        resultSet = statement.executeQuery();
		       
		        while (resultSet.next()) {
		        	
		            Conta contaLocal = new Conta();
		            contaLocal.setId(resultSet.getInt("id"));
		          
		            SaldoDAO saldoConta = new SaldoDAO();
		            saldoConta.setBd(bd);
		            saldoConta.setConta(contaLocal);
		            
		            if (saldoConta.localizar()) {
		                contaLocal.setSaldo(saldoConta.getSaldo()); 
		            } else {
		                contaLocal.setSaldo(0.0);
		            }
		            
		            if (this.usuario instanceof Cliente) {
		                contaLocal.setCliente((Cliente) this.usuario);
		            } else {
		                System.out.println("Aviso: O usuário logado não é um Cliente.");
		            }
		            
		            contaLocal.setDataCadastro(resultSet.getTimestamp("dataCadastro").toLocalDateTime().toLocalDate());
		            
		            listaContas.add(contaLocal);
		        }

		        return listaContas;

		    } catch (SQLException erro) {
		        erro.printStackTrace();
		        return null;
		    }
		}
		
		
		/////////// IMPLEMENTAR
		public boolean localizar() {
			return false;
		}
	
		public String atualizar(TipoOperacaoBD operacao) {
			return "teste";
		}
		//////////////////////////
	}