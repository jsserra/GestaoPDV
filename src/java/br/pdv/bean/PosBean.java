/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.bean;

import br.pdv.dao.PosDAO;
import br.pdv.dao.UsuarioDAO;
import br.pdv.model.Pos;
import br.pdv.model.Usuario;
import br.pdv.model.enums.Bandeira;
import br.pdv.model.enums.Chip;
import br.pdv.model.enums.Locale;
import br.pdv.model.enums.ModeloPos;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author julianos
 */
@Named
@ViewScoped
public class PosBean implements Serializable {

    private Pos pos = new Pos();
    private Pos posDesativado = new Pos();
    private Usuario usuario = new Usuario();

    private Bandeira band;
    private Chip chip;
    private ModeloPos modelo;
    private Locale locale;

    private String item;
    private String msg;
    private Integer usuarioId;

    private List<Pos> posSubstituido = new ArrayList<>();

    @Inject
    private PosDAO daoPos;

    @Inject
    private UsuarioDAO daoUsuario;

    // GET & SET
    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public Pos getPosDesativado() {
        return posDesativado;
    }

    public void setPosDesativado(Pos posDesativado) {
        this.posDesativado = posDesativado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Bandeira getBand() {
        return band;
    }

    public void setBand(Bandeira band) {
        this.band = band;
    }

    public Chip getChip() {
        return chip;
    }

    public void setChip(Chip chip) {
        this.chip = chip;
    }

    public ModeloPos getModelo() {
        return modelo;
    }

    public void setModelo(ModeloPos modelo) {
        this.modelo = modelo;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Pos> getPosSubstituido() {
        return posSubstituido;
    }

    public void setPosSubstituido(List<Pos> posSubstituido) {
        this.posSubstituido = posSubstituido;
    }

    //ACTiONS
    public String novoPos() throws pdvException {
        this.pos = new Pos();

        Boolean onNew = true;
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

        flash.put("pos", pos);
        flash.put("posDesativado", this.posDesativado);
        flash.put("oneNew", onNew);

        return "formPos?faces-redirect=true";
    }

    public String salvar() throws pdvException {

        if (usuarioId == null) {
            this.usuario = daoUsuario.buscarId(1);
        } else {
            this.usuario = daoUsuario.buscarId(usuarioId);
        }

        if (this.band == null) {
            band = Bandeira.cielo;
        }

        if (this.chip == null) {
            this.chip = Chip.Vivo;
        }
        if (this.modelo == null) {
            this.modelo = ModeloPos.MOVE5000;
        }
        if (this.locale == null) {
            this.locale = Locale.Recepção;
        }

        pos.setUsuario(usuario);
        pos.setBandeira(this.band);
        pos.setChip(chip);
        pos.setModelo(modelo);
        pos.setLocale(locale);

        if (this.pos.getId() == null) {

            daoPos.salvar(this.pos);
        } else {
            daoPos.atualizar(this.pos);
        }

        if (this.posDesativado.getId() != null) {
            daoPos.dasativar(posDesativado);
        }

        pos = new Pos();
        posDesativado = new Pos();
        posSubstituido = new ArrayList<>();

        return "listaPos?faces-redirect=true";
    }

    public String editar(Pos pos) throws pdvException {

        this.pos = pos;

        Boolean on = true;
        Boolean onSub = false;
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

        flash.put("pos", pos);
        flash.put("modelo", pos.getModelo());
        flash.put("local", pos.getLocale());
        flash.put("bandeira", pos.getBandeira());
        flash.put("chip", pos.getChip());
        flash.put("user", pos.getUsuario().getId());
        flash.put("on", on);
        flash.put("onSub", onSub);

        if (pos.getSubstituta() == null) {
            return "formPos?faces-redirect=true";
        } else {
            this.posSubstituido.add(daoPos.buscarId(pos.getSubstituta()));

            onSub = true;
            flash.put("onSub", onSub);
            flash.put("posSubstituido", this.posSubstituido);

            return "formPosTroca?faces-redirect=true";
        }

    }

    public void carregar() throws pdvException {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        Boolean on = (Boolean) flash.get("on");
        Boolean onSub = (Boolean) flash.get("onSub");
        Boolean onNew = (Boolean) flash.get("onNew");
        if (onNew != null && onNew == true) {
            this.pos = (Pos) flash.get("pos");
            this.posDesativado = (Pos) flash.get("posDesativado");

        }
        if (on != null && on == true && onSub == false) {
            this.pos = (Pos) flash.get("pos");
            this.modelo = (ModeloPos) flash.get("modelo");
            this.locale = (Locale) flash.get("local");
            this.band = (Bandeira) flash.get("bandeira");
            this.chip = (Chip) flash.get("chip");
            this.usuarioId = (Integer) flash.get("user");

            usuario = daoUsuario.buscarId(usuarioId);
            pos.setModelo(modelo);
            pos.setLocale(locale);
            pos.setBandeira(band);
            pos.setChip(chip);
            pos.setUsuario(usuario);

        } else if (on != null && on == true && onSub == true) {
            this.pos = (Pos) flash.get("pos");
            this.modelo = (ModeloPos) flash.get("modelo");
            this.locale = (Locale) flash.get("local");
            this.band = (Bandeira) flash.get("bandeira");
            this.chip = (Chip) flash.get("chip");
            this.usuarioId = (Integer) flash.get("user");
            this.posSubstituido = (List<Pos>) flash.get("posSubstituido");

            usuario = daoUsuario.buscarId(usuarioId);
            pos.setModelo(modelo);
            pos.setLocale(locale);
            pos.setBandeira(band);
            pos.setChip(chip);
            pos.setUsuario(usuario);

        } else {
            this.pos = (Pos) flash.get("pos");
            this.posDesativado = (Pos) flash.get("posDesativado");
            this.posSubstituido = (List<Pos>) flash.get("posSubstituido");
        }
    }

    public String remover(Pos pos) throws pdvException {
        daoPos.excluir(pos);
        return "listaPos?faces-redirect=true";
    }

    public String substituir(Pos pos) throws pdvException {

        this.pos = new Pos();
        this.pos.setSubstituta(pos.getId());
        //daoPos.dasativar(pos);
        this.posDesativado = daoPos.buscarId(pos.getId());

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        this.posSubstituido.add(daoPos.buscarId(pos.getId()));
        flash.put("posSubstituido", this.posSubstituido);
        flash.put("posDesativado", this.posDesativado);
        flash.put("pos", this.pos);
        return "formPosTroca?faces-redirect=true";
    }

    public String desativar(Pos pos) throws pdvException {
        daoPos.dasativar(pos);
        return "listaPos?faces-redirect=true";
    }

    public List<Pos> getListaPosMatriz() throws pdvException {
        return daoPos.listaPosMatriz();

    }

    public List<Pos> getListaPosFilial() throws pdvException {
        return daoPos.listaPosFilial();
    }

    public List<Usuario> getListaUsuarios() throws pdvException {
        return daoUsuario.listar();
    }

    public Integer getQtdPosMatriz() throws pdvException {
        return daoPos.listaPosMatriz().size();
    }

    public Integer getQtdPosMatrizCielo() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosMatriz().stream().filter(x -> (x.getBandeira() == Bandeira.cielo)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosMatrizRede() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosMatriz().stream().filter(x -> (x.getBandeira() == Bandeira.rede)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosMatrizGetnet() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosMatriz().stream().filter(x -> (x.getBandeira() == Bandeira.getnet)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosMatrizBraxcard() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosMatriz().stream().filter(x -> (x.getBandeira() == Bandeira.braXcard)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosFilial() throws pdvException {
        return daoPos.listaPosFilial().size();
    }

    public Integer getQtdPosFilialCielo() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosFilial().stream().filter(x -> (x.getBandeira() == Bandeira.cielo)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosFilialRede() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosFilial().stream().filter(x -> (x.getBandeira() == Bandeira.rede)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosFilialGetnet() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosFilial().stream().filter(x -> (x.getBandeira() == Bandeira.getnet)).collect(Collectors.toList());
        return qtdCielo.size();
    }

    public Integer getQtdPosFilialBraxcard() throws pdvException {
        List<Pos> qtdCielo = daoPos.listaPosFilial().stream().filter(x -> (x.getBandeira() == Bandeira.braXcard)).collect(Collectors.toList());
        return qtdCielo.size();
    }

}
