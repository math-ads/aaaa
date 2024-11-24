package model;

public class Disciplina {

    private String codigoDisciplina;
    private String nomeDisciplina;
    private String diaSemana;
    private int horaInicio;
    private int quantHoras;
    private String codigoCurso;

    public Disciplina(String codigoDisciplina, String nomeDisciplina, String diaSemana, int horaInicio, int quantHoras, String codigoCurso) {
        this.codigoDisciplina = codigoDisciplina;
        this.nomeDisciplina = nomeDisciplina;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.quantHoras = quantHoras;
        this.codigoCurso = codigoCurso;
    }
    
    public Disciplina () {
    	this ("","","",0,0,"");
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        if (horaInicio >= 0 && horaInicio <= 23) {
            this.horaInicio = horaInicio;
        } else {
            throw new IllegalArgumentException("Horário inicial deve estar entre 0 e 23.");
        }
    }

    public int getQuantHoras() {
        return quantHoras;
    }

    public void setQuantHoras(int quantHoras) {
        if (quantHoras > 0) {
            this.quantHoras = quantHoras;
        } else {
            throw new IllegalArgumentException("Quantidade de horas deve ser positiva.");
        }
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String toString() {
        return codigoDisciplina + ";" + nomeDisciplina + ";" + diaSemana + ";" + horaInicio + ";" + quantHoras + ";" + codigoCurso;
    }
}
