/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.model.Celular;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.hibernate.HibernateException;

/**
 *
 * @author julianos
 */
public class CelularDAO implements Serializable {

    @Inject
    EntityManager em;

    private GeneralDAO<Celular> dao;

    @PostConstruct
    void start() {
        this.dao = new GeneralDAO(this.em, Celular.class);
    }

    public void salvar(Celular celular) throws pdvException {
        dao.salvar(celular);
    }

    public void atualizar(Celular celular) throws pdvException {
        dao.atualizar(celular);
    }

    public void excluir(Celular celular) throws pdvException {
        dao.excluir(celular);
    }

    public List<Celular> listar() throws pdvException {
        List<Celular> lista = null;
        try{
            em.getTransaction().begin();
            lista = em.createQuery("select c from Celular c order by c.modelo").getResultList();
            em.getTransaction().commit();
        }catch(HibernateException he){
            if(em.isOpen() && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new pdvException("Falha ao listar celulares " + he.getMessage());
        }
        return lista;
    }
    
    public List<Celular> listarTodos() throws pdvException{
        return dao.listar();
    }

    public Celular buscarId(Integer id) throws pdvException {
        return dao.buscarId(id);
    }

    public void desativar(Celular celular) throws pdvException {
        try {
            em.getTransaction().begin();
            celular.setAtivo(Boolean.FALSE);
            em.getTransaction().commit();
        } catch (HibernateException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            throw new pdvException("Erro ao desativar celular " + ex.getMessage());
        }
    }

}
