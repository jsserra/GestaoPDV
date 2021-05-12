/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.dao;

import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;

/**
 *
 * @author julianos
 * @param <T>
 */
public class GeneralDAO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EntityManager entityManager;
    private final Class<T> classe;

    public GeneralDAO(EntityManager entityManager, Class<T> classe) {
        this.entityManager = entityManager;
        this.classe = classe;
    }

    public void salvar(T tipo) throws pdvException {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(tipo);
            entityManager.getTransaction().commit();
        } catch (HibernateException he) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new pdvException("Erro ao gravar no banco" + he.getMessage());
        }
    }

    public void excluir(T tipo) throws pdvException {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(tipo);
            entityManager.getTransaction().commit();
        } catch (HibernateException he) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new pdvException("Erro ao gravar no banco" + he.getMessage());
        }
    }

    public List<T> listar() throws pdvException {
        List<T> lista = null;
        try {
            entityManager.getTransaction().begin();
            CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(classe);
            query.select(query.from(classe));
            lista = entityManager.createQuery(query).getResultList();
            entityManager.getTransaction().commit();
        } catch (HibernateException he) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new pdvException("Erro ao listar dados do banco gestaoti: " + he.getMessage());
        }
        return lista;
    }

    public void atualizar(T tipo) throws pdvException {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(tipo);
            entityManager.getTransaction().commit();
        } catch (HibernateException he) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new pdvException("Erro ao gravar no banco" + he.getMessage());
        }
    }

    public T buscarId(Integer id) throws pdvException {
        T tipo = null;
        try {
            entityManager.getTransaction().begin();
            tipo = entityManager.find(classe, id);
            entityManager.getTransaction().commit();
        } catch (HibernateException he) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new pdvException("Erro buscar Id" + he.getMessage());
        }
        return tipo;
    }
    
    

}
