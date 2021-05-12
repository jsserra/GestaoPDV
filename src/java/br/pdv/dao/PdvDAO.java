/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.bean.ComputerBean;
import br.pdv.model.Computer;
import br.pdv.model.Pdv;
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
public class PdvDAO implements Serializable {

    @Inject
    EntityManager em;

    private GeneralDAO<Pdv> dao;

    @PostConstruct
    void start() {
        this.dao = new GeneralDAO<>(this.em, Pdv.class);
    }

    public void salvar(Pdv pdv) throws pdvException {
        dao.salvar(pdv);
    }

    public List<Pdv> listar() throws pdvException {
        return dao.listar();
    }

    public Pdv buscarId(Integer id) throws pdvException {
        return dao.buscarId(id);
    }

    public List<Pdv> listaPdvMatriz() throws pdvException {
        List<Pdv> lista = null;
        em.getTransaction().begin();
        try {
            Query q = em.createQuery("select p from Pdv p where p.filial = 0");
            lista = q.getResultList();
            em.getTransaction().commit();
        } catch (HibernateException h) {
            if (em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new pdvException("Erro ao listar PDV " + h.getMessage());
        }
        return lista;
    }

    public List<Pdv> listaPdvMatrizLivre() throws pdvException {
        List<Pdv> list = listaPdvMatriz();
        List<Pdv> pdvs = listaPdvMatriz();
        List<Computer> pcs = new ComputerDAO().listarPcMatriz();
        ComputerBean bean = new ComputerBean();
        //em.getTransaction().begin();
        try {
            list.forEach((Pdv p) -> {
                pcs.stream().filter(pc -> (p.equals(pc.getPdv()))).forEachOrdered(_item -> {
                    pdvs.remove(p);
                });
            });
        } catch (HibernateException he) {
            if (em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new pdvException("Erro ao listar PDV Matriz" + he.getMessage());
        }
        return pdvs;
    }

    public List<Pdv> listaPdvFilial() throws pdvException {
        List<Pdv> lista = null;
        em.getTransaction().begin();
        try {
            Query q = em.createQuery("select p from Pdv p where p.filial = 1");
            lista = q.getResultList();
            em.getTransaction().commit();
        } catch (HibernateException h) {
            if (em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new pdvException("Erro ao listar PDV " + h.getMessage());
        }
        return lista;
    }

    public List<Pdv> listaPdvFilialLivre() throws pdvException {
        List<Pdv> list = listaPdvFilial();
        List<Pdv> pdvs = listaPdvFilial();
        List<Computer> pcs = new ComputerDAO().listarPcFilial();
        em.getTransaction().begin();
        try {
            list.forEach((Pdv p) -> {
                pcs.stream().filter(pc -> (p.equals(pc.getPdv()))).forEachOrdered(_item -> {
                    pdvs.remove(p);
                });
            });
            em.getTransaction().commit();
        } catch (HibernateException he) {
            if (em.isOpen() || em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new pdvException("Erro os listar PDV filial" + he.getMessage());
        }
        return pdvs;
    }

}
