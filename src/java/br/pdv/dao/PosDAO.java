/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.model.Pos;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.hibernate.HibernateException;

/**
 *
 * @author julianos
 */
public class PosDAO implements Serializable {

    @Inject
    EntityManager em;

    private GeneralDAO<Pos> dao;

    @PostConstruct
    void start() {
        dao = new GeneralDAO(this.em, Pos.class);
    }

    public void salvar(Pos pos) throws pdvException {
        dao.salvar(pos);
    }

    public void atualizar(Pos pos) throws pdvException {
        dao.atualizar(pos);
    }

    public void excluir(Pos pos) throws pdvException {
        dao.excluir(pos);
    }

    public List<Pos> listar() throws pdvException {
        return dao.listar();
    }

    public Pos buscarId(Integer id) throws pdvException {
        return dao.buscarId(id);
    }

 /*   public List<Pos> listaPosMatriz() throws pdvException {
        List<Pos> posMatriz = listar().stream().filter(x -> (x.getFilial() == 0)&&(x.isAtivo() == Boolean.TRUE)).collect(Collectors.toList());
        return posMatriz;
    }

    public List<Pos> listaPosFilial() throws pdvException {
        return listar().stream().filter(x -> (x.getFilial() == 1) && (x.isAtivo() == Boolean.TRUE)).collect(Collectors.toList());
    }
    */
    
    public List<Pos> listaPosMatriz() throws pdvException{
        List<Pos> poss = null;
        try{
            em.getTransaction().begin();
            poss = em.createQuery("select p from Pos p where p.filial = 0 and p.ativo = 1 order by p.bandeira, p.chip").getResultList();
            em.getTransaction().commit();
        } catch(HibernateException he){
            if(em.isOpen() && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new pdvException("Erro ao listar POS: " + he.getMessage());
        }
        return poss;
    }
    
        public List<Pos> listaPosFilial() throws pdvException{
        List<Pos> poss = null;
        try{
            em.getTransaction().begin();
            poss = em.createQuery("select p from Pos p where p.filial = 1 and p.ativo = 1 order by p.bandeira, p.chip").getResultList();
            em.getTransaction().commit();
        } catch(HibernateException he){
            if(em.isOpen() && em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new pdvException("Erro ao listar POS: " + he.getMessage());
        }
        return poss;
    }

    public void dasativar(Pos pos) throws pdvException {
        pos.setAtivo(Boolean.FALSE);
        try {
            em.getTransaction().begin();
            em.merge(pos);
            em.getTransaction().commit();
        } catch (HibernateException he) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new pdvException("Falha ao desativar POS: " + he.getMessage());
        }
    }
}
