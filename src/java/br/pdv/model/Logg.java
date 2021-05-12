package br.pdv.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "logg")
public class Logg implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipoLog")
    private String TipoLog;
    /*@Enumerated(EnumType.STRING)
    private TipoLog tipoLog;*/

    private String descricao;

    @Column(name = "datahora")
    private LocalDateTime data;

    @ManyToOne
    //@JoinColumn(name = "idpc", referencedColumnName = "id")
    private Computer computer;

    public Logg() {
    }    

    public Logg(String descricao, LocalDateTime data) {
        this.descricao = descricao;
        this.data = data;
    }

    public Logg(String TipoLog, String descricao) {
        this.TipoLog = TipoLog;
        this.descricao = descricao;
    }
    
    

    public Logg(String descricao, LocalDateTime data, Computer computer) {
        this.descricao = descricao;
        this.data = data;
        this.computer = computer;
    }    
    
   public Logg(String tipoLog, String descricao, LocalDateTime data) {
        this.TipoLog = tipoLog;
        this.descricao = descricao;
        this.data = data;
    }

    public Logg(String TipoLog, String descricao, LocalDateTime data, Computer computer) {
        this.TipoLog = TipoLog;
        this.descricao = descricao;
        this.data = data;
        this.computer = computer;
    }
   
   
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoLog() {
        return TipoLog;
    }

    public void setTipoLog(String TipoLog) {
        this.TipoLog = TipoLog;
    }
    
    

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setPc(Computer computer) {
        this.computer = computer;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Log{" + "id=" + id  + ", descricao=" + descricao + ", data=" + data + "}";//, pc=" + computer.getNome() + '}';
    }
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Logg other = (Logg) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
