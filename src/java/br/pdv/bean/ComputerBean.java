/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pdv.bean;

import br.pdv.dao.ComputerDAO;
import br.pdv.dao.LogDAO;
import br.pdv.dao.PdvDAO;
import br.pdv.model.Computer;
import br.pdv.model.Logg;
import br.pdv.model.Pdv;
import br.pdv.model.enums.TipoLog;
import br.pdv.util.pdvException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author julianos
 */
@Named
@ViewScoped
public class ComputerBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Computer computer = new Computer();
    private Pdv pdv = new Pdv();

    private List<Logg> logPC = new ArrayList<>();

    private Integer idPdv;
    private Integer logId;
    private String logItem;
    private String logItemTipo;
    private String windows;
    private boolean logEstaVazio = true;

    @Inject
    private ComputerDAO daoComputer;

    @Inject
    private LogDAO daoLog;

    @Inject
    private PdvDAO daoPdv;

    // GET & SET
    public List<Logg> getLogPC() {
        return logPC;
    }

    public void setLogPC(List<Logg> logPC) {
        this.logPC = logPC;
    }

    public Integer getIdPdv() {
        return idPdv;
    }

    public void setIdPdv(Integer idPdv) {
        this.idPdv = idPdv;
    }

    public boolean isLogEstaVazio() {
        return logEstaVazio;
    }

    public void setLogEstaVazio(boolean logEstaVazio) {
        this.logEstaVazio = logEstaVazio;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public String getLogItem() {
        return logItem;
    }

    public void setLogItem(String logItem) {
        this.logItem = logItem;
    }

    public String getLogItemTipo() {
        return logItemTipo;
    }

    public void setLogItemTipo(String logItemTipo) {
        this.logItemTipo = logItemTipo;
    }

    public String getWindows() {
        return windows;
    }

    public void setWindows(String windows) {
        this.windows = windows;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    // ACTIONS
    public List<Computer> getListarComputer() throws pdvException {
        return daoComputer.listar();
    }

    public List<Computer> getListarComputerMatriz() throws pdvException {
        return daoComputer.listarPcMatriz();
    }

    public List<Computer> getListarComputerFilial() throws pdvException {
        return daoComputer.listarPcFilial();
    }

    public Integer getQtdLogPc(Computer computer) throws pdvException {
        return daoLog.listaLogPorPc(computer.getId()).size();
    }

    public String novoPcMatriz() {
        this.computer = new Computer();
        this.logPC = new ArrayList<>();
        return "formCaixaMatriz?faces-redirect=true";
    }

    public String novoPcFilial() {
        this.computer = new Computer();
        this.logPC = new ArrayList<>();
        return "formCaixaFilial?faces-redirect=true";
    }

    public void carregar() throws pdvException {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        Boolean on = (Boolean) flash.get("on");
        if (on != null && on == true) {
            this.computer = (Computer) flash.get("pc");
            this.logPC = (List<Logg>) flash.get("logs"); //novo
            this.idPdv = (Integer) flash.get("pdv");
            this.logEstaVazio = (Boolean) flash.get("logEstaVazio");
            // this.windows = (String) flash.get("so");

           /* pdv = daoPdv.buscarId(idPdv);
          
            computer.setPdv(pdv);*/
            
        }
    }

    public String editarPC(Computer pc) throws pdvException {
        this.computer = pc;
        this.logPC = new ArrayList<>(this.computer.getLogs());

        if (logPC.isEmpty()) {
            logEstaVazio = true;
        } else {
            logEstaVazio = false;
        }

        Boolean on = true;
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

        flash.put("pc", this.computer);
        flash.put("logs", logPC);
        flash.put("pdv", pc.getPdv().getId());
        flash.put("on", on);
        flash.put("logEstaVazio", logEstaVazio);

        if (pc.getPdv().getFilial() == 0) {
            return "formCaixaMatriz?faces-redirect=true";
        } else {
            return "formCaixaFilial?faces-redirect=true";
        }
    }

    public String gravarPc() throws pdvException {

        pdv = daoPdv.buscarId(this.idPdv);
        computer.setPdv(pdv);

        if (!this.logPC.isEmpty()) {
            computer.setLogs(this.logPC);
            if (computer.getId() == null) {
                daoComputer.salvar(computer);
            } else {
                daoComputer.atualizar(computer);
            }

            /* for (Log l : logPC) {
                l.setPc(computer);
                daoLog.salvar(l);
            }*/
            Computer pcTemp = computer;
            this.computer = new Computer();
            this.logPC = new ArrayList<>();
            if (pcTemp.getPdv().getFilial() == 0) {
                return "index?faces-redirect=true";
            } else {
                return "caixaFilial?faces-redirect=true";
            }
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("Log",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insira Log de inclusão!", ""));
            return "";
        }
    }

    public void desativarPc(Computer pc) throws pdvException {
        daoComputer.desativarPc(pc);
    }

    public String excluircPC(Computer pc) throws pdvException {
        Computer pcTemp = pc;
        daoComputer.excluir(pc);
        if (pcTemp.getPdv().getFilial() == 0) {
            return "index?faces-redirect=true";
        } else {
            return "caixaFilial?faces-redirect=true";
        }
    }

    public List<TipoLog> getTipoLogs() {

        List<TipoLog> tipoLogs = Arrays.asList(TipoLog.values());
        return tipoLogs;
    }

    public List<String> getTiposLogs() {
        return Arrays.asList("Incluir", "Troca", "Desativar", "Envio Ajuda", "Envio Matriz",
                "Conexão", "BD", "Hardware", "SASIII", "Rede", "Site Receita", "Stress", "Windows");
    }

    public List<String> getSO() {
        return Arrays.asList("win7 pro x86", "win7 pro x64", "win10 pro x86", "win10 pro x64");
    }

    //ACTiONS of LOG
    /* public void adicionarLog() throws pdvException {
        this.logPC.add(new Log(logItemTipo, logItem, LocalDateTime.now()));
        this.logItemTipo = null;
        this.logItem = null;
        setLogEstaVazio(false);
    }*/
    public void adicionarLog() throws pdvException {
        //Log logSelecionado = daoLog.buscarId(logId);
        // Logg LogNovo = new Logg(this.logItemTipo, this.logItem, LocalDateTime.now(), this.computer);
        //daoLog.salvar(LogNovo);
        this.logPC.add(new Logg(this.logItemTipo, this.logItem, LocalDateTime.now()));
        //this.logPC.add(LogNovo);
        this.logItemTipo = null;
        this.logItem = null;
        setLogEstaVazio(false);
    }

    public void apagarLos(Logg log) throws pdvException {

        this.logPC.remove(log);

        if (logPC.isEmpty()) {
            setLogEstaVazio(true);
        }
    }

    //ACTiONS of PDV
    public List<Pdv> getListarPdvsMatriz() throws pdvException {
        List<Computer> pcs = getListarComputerMatriz().stream().filter(x -> (x.getPdv() != null)).collect(Collectors.toList());
        List<Pdv> pdvs = daoPdv.listaPdvMatriz();
        List<Pdv> lista = new ArrayList<>();
       // List<Pdv> pdvLivre = new ArrayList<>();
        for (Computer c : pcs) {
            lista.add(c.getPdv());
        }
        for (Pdv p : lista) {
            pdvs.remove(p);
        }
        if (this.computer.getPdv() != null) {
            pdvs.add(this.computer.getPdv());
        }

        System.out.println("PCs com pdv null = " + pcs.size());
        System.out.println("PDVs = " + pdvs.size());

        return pdvs;
    }

    public List<Pdv> getListarPdvsFilial() throws pdvException {
        List<Computer> pcs = getListarComputerFilial().stream().filter(x -> (x.getPdv() != null)).collect(Collectors.toList());
        List<Pdv> pdvsFilial = daoPdv.listaPdvFilial();
        List<Pdv> lista = new ArrayList<>();
        for (Computer c : pcs) {
            lista.add(c.getPdv());
        }
        for (Pdv p : lista) {
            pdvsFilial.remove(p);
        }
        if (this.computer.getPdv() != null) {
            pdvsFilial.add(this.computer.getPdv());
        }

        return pdvsFilial;
    }

    public void ordenarLogs(String ordem) {
        if (ordem.equals("az")) {
            logPC.sort(Comparator.comparing(Logg::getTipoLog));
        } else {
            logPC.sort(Comparator.comparing(Logg::getTipoLog).reversed());
        }
    }

    public void atualizarPdv(AjaxBehaviorEvent e) throws pdvException {
        pdv = daoPdv.buscarId(idPdv);
    }

}
