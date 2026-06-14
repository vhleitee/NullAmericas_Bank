package model;

import java.time.LocalDateTime;

public class Saldo {

    private Double saldo;
    private LocalDateTime dataConsulta;

    public Saldo() {
        this.setSaldo(0.0);
    }

    public Saldo(Double saldo) {
        this.setSaldo(saldo);
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        if (saldo == null) {
            this.saldo = 0.0;
        } else {
            this.saldo = saldo;
        }
        this.updateDataConsulta();
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    private void updateDataConsulta() {
        this.dataConsulta = LocalDateTime.now();
    }
}