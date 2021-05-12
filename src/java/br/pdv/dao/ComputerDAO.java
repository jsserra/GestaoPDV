/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.model.Computer;
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
public class ComputerDAO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    private GeneralDAO<Computer> dao;

    @PostConstruct
    void start() {
        dao = new GeneralDAO<>(this.em, Computer.class);
    }

    public void salvar(Computer pc) throws pdvException {
        dao.salvar(pc);
    }

    public void atualizar(Computer pc) throws pdvException {
        dao.atualizar(pc);
    }

    public void excluir(Computer pc) throws pdvException {
        dao.excluir(pc);
    }

    public Computer buscarId(Integer id) throws pdvException {
        return dao.buscarId(id);
    }

    public List<Computer> listar() throws pdvException {
        return dao.listar();
    }

    public List<Computer> listarPcMatriz() throws pdvException {
        List<Computer> lista = null;
        Query q = null;
        try {
            em.getTransaction().begin();
            q = em.createQuery("select c from Computer c where c.ativo = 1 and c.pdv.filial = 0 order by c.pdv.nome, c.nome");
            lista = q.getResultList();
            em.getTransaction().commit();
        } catch (HibernateException he) {
            if (em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erro ao Listar os PCs dos Caixas " + he.getMessage());
        }
        return lista;
    }

    public List<Computer> listarPcFilial() throws pdvException {
        List<Computer> lista = null;
        Query q = null;
        try {
            em.getTransaction().begin();
            q = em.createQuery("select c from Computer c where c.ativo = 1 and c.pdv.filial = 1 order by c.pdv.nome, c.nome");
            lista = q.getResultList();
            em.getTransaction().commit();
        } catch (HibernateException he) {
            if (em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erro ao Listar os PCs dos Caixas " + he.getMessage());
        }
        return lista;
    }

    public void desativarPc(Computer pc) throws pdvException {
        pc.setPdv(null);
        pc.setAtivo(Boolean.FALSE);
        try {
            em.getTransaction().begin();
            em.merge(pc);
            em.getTransaction().commit();
        } catch (HibernateException he) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new pdvException("Falha ao desativar pc " + he.getMessage());
        }
    }
}
