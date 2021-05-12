/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.model.Logg;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.HibernateException;

/**
 *
 * @author julianos
 */
public class LogDAO implements Serializable {

    @Inject
    EntityManager em;

    private GeneralDAO<Logg> dao;

    @PostConstruct
    void satrt() {
        this.dao = new GeneralDAO<>(this.em, Logg.class);
    }

    public void salvar(Logg log) throws pdvException {
        dao.salvar(log);
    }

    public void atualizar(Logg log) throws pdvException {
        dao.atualizar(log);
    }

    public void excluir(Logg log) throws pdvException {
        dao.excluir(log);
    }

    public List<Logg> listar() throws pdvException {
        return dao.listar();
    }

    public Logg buscarId(Integer id) throws pdvException {
        return dao.buscarId(id);
    }

    public List<Logg> listaLogPorPc(Integer id) throws pdvException {
        List<Logg> lista = null;
        Query q = null;
        em.getTransaction().begin();
        try {
            q = em.createQuery("select l from Logg l where l.computer.id = :id");
            q.setParameter("id", id);
            lista = q.getResultList();
            em.getTransaction().commit();
        } catch (HibernateException he) {
            if (em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erro ao Listar Logs: " + he.getMessage());
        }
        return lista;
    }
}
