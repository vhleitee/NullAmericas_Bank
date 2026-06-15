	package repository;
	
	import enums.TipoOperacaoBD;
	import enums.TipoTransacao;
	
	import java.sql.*;
	import java.util.ArrayList;
	import java.util.List;
	
	import model.BD;
	import model.Transacao;
	import model.Conta;
	
	public class TransacaoDAO {
		private BD bd;
		private Conta conta;
		private Transacao trasacao;
		
		private String sql, msg;
	
		public Transacao getTrasacao() {
			return trasacao;
		}

		public void setTrasacao(Transacao trasacao) {
			this.trasacao = trasacao;
		}

		private PreparedStatement statement;
		private ResultSet resultSet;
	
		public TransacaoDAO() {
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
	
		public List<Transacao> localizarTransacoes() {
			if (this.getConta() == null) {
				System.out.println("Nenhuma conta associada ao TransacaoDAO.");
				return null;
			}
	
			String sql = "SELECT * FROM transacao WHERE idConta = ?";
			List<Transacao> listaTransacoes = new ArrayList<>();
	
			try {
				statement = bd.connection.prepareStatement(sql);
				statement.setInt(1, this.getConta().getId());
	
				resultSet = statement.executeQuery();
	
				while (resultSet.next()) {
					Transacao atual = new Transacao();
					atual.setData(resultSet.getTimestamp("dataTransacao").toLocalDateTime());
					atual.setId(resultSet.getInt("id"));
					atual.setIdConta(resultSet.getInt("idConta"));
					atual.setIdContaCorrespondente(resultSet.getInt("idContaCorrespondente"));
					atual.setTipoTransacao(TipoTransacao.valueOf(resultSet.getString("tipoTransacao")));
					atual.setValor(resultSet.getDouble("valor"));
					listaTransacoes.add(atual);
				}
	
				this.getConta().getTransacoes().clear(); // Limpa o conteúdo do objeto atual
				this.getConta().getTransacoes().addAll(listaTransacoes); // Adiciona os novos itens no mesmo objeto
	
				return listaTransacoes;
	
			} catch (SQLException erro) {
				erro.printStackTrace();
				return null;
			}
		}
	
		public String atualizar(TipoOperacaoBD operacao) {
		    msg = "Operação realizada com sucesso!";
		    try {
		        if (operacao == TipoOperacaoBD.INCLUSAO) {
		            
		            if (this.trasacao.getTipoTransacao() == TipoTransacao.TRANSFERENCIA_ENVIADA) {
		                // Ajustado para inserir as duas linhas da transferência de forma explícita
		                sql = "INSERT INTO transacao (idConta, idContaCorrespondente, valor, tipoTransacao) VALUES " +
		                      "(?, ?, ?, 'TRANSFERENCIA_ENVIADA'), " +
		                      "(?, ?, ?, 'TRANSFERENCIA_RECEBIDA')";
		                
		                statement = bd.connection.prepareStatement(sql);
		                statement.setInt(1, trasacao.getIdConta());
		                statement.setInt(2, trasacao.getIdContaCorrespondente());
		                statement.setDouble(3, -Math.abs(trasacao.getValor())); // Negativo para quem envia
		                statement.setInt(4, trasacao.getIdContaCorrespondente());
		                statement.setInt(5, trasacao.getIdConta());
		                statement.setDouble(6, Math.abs(trasacao.getValor()));  // Positivo para quem recebe
		                
		            } else if (this.trasacao.getTipoTransacao() == TipoTransacao.DEPOSITO) {
		                sql = "INSERT INTO transacao (idConta, idContaCorrespondente, valor, tipoTransacao) VALUES (?, ?, ?, 'DEPOSITO')";
		                
		                statement = bd.connection.prepareStatement(sql);
		                statement.setInt(1, trasacao.getIdConta());
		                // No depósito, a conta correspondente pode ser ela mesma ou nula (usaremos o ID da própria conta)
		                statement.setInt(2, trasacao.getIdConta()); 
		                statement.setDouble(3, Math.abs(trasacao.getValor())); // Sempre positivo
		                
		            } else if (this.trasacao.getTipoTransacao() == TipoTransacao.SAQUE) {
		                sql = "INSERT INTO transacao (idConta, idContaCorrespondente, valor, tipoTransacao) VALUES (?, ?, ?, 'SAQUE')";
		                
		                statement = bd.connection.prepareStatement(sql);
		                statement.setInt(1, trasacao.getIdConta());
		                statement.setInt(2, trasacao.getIdConta()); 
		                statement.setDouble(3, -Math.abs(trasacao.getValor())); // Sempre negativo
		            }
		        }
		
		        if (statement != null && statement.executeUpdate() == 0) {
		            msg = "Falha na operação!";
		        }
		    }
		    catch (SQLException erro) {
		        msg = "Falha na operação - " + erro.toString();
		    }
		
		    return msg;
		}
	}