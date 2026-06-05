package model;
import enums.TipoOperacaoBD;

public interface OperacaoBD {
	public boolean localizar();
	public String atualizar(TipoOperacaoBD operacao);
}
