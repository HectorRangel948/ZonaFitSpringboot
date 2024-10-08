package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//@Component
public class ZonaFitForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private JLabel idLabel;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaDeModeloClientes;
    private Integer idCliente;

    @Autowired
    public ZonaFitForma(IClienteServicio clienteServicio){
        this.clienteServicio = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> eliminarCliente());
        limpiarButton.addActionListener(e -> limpiarFormulario());
    }

    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);
    }




    private void createUIComponents() {
        // TODO: place custom component creation code here
        //this.tablaDeModeloClientes = new DefaultTableModel(0,4);

        //Manera de crear la tabla sinque las celdas sean editables
        this.tablaDeModeloClientes = new DefaultTableModel(0,4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeceros = {"ID", "Nombre", "Apellido", "Membresía"};
        this.tablaDeModeloClientes.setColumnIdentifiers(cabeceros);
        this.clientesTabla = new JTable(tablaDeModeloClientes);
        //Restringimos la selección de la tabla a un sólo registro
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Cargar listado de clientes
        listarClientes();
    }

    private void listarClientes(){
        this.tablaDeModeloClientes.setRowCount(0);
        var clientes = this.clienteServicio.listarClientes();
        clientes.forEach((cliente)->{
            Object[] renglonCliente ={
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.tablaDeModeloClientes.addRow(renglonCliente);
        });
    }

    private void guardarCliente(){
        if(nombreTexto.getText().equals("")){
            mostrarMensaje("Proporciona un nombre");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if(membresiaTexto.getText().equals("")){
            mostrarMensaje("Proporcione un valor para la membresía");
            membresiaTexto.requestFocusInWindow();
            return;
        }

        //Recuperamos los valores del formulario
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());
        Cliente cliente = new Cliente(this.idCliente, nombre, apellido, membresia);
        this.clienteServicio.guardarCliente(cliente);
        if(this.idCliente==null){
            mostrarMensaje("Se creó con éxito un nuevo cliente");
        }
        else{mostrarMensaje("Se actualizó con éxito el cliente con id: "+ this.idCliente);}
        limpiarFormulario();
        listarClientes();
    }

    private void cargarClienteSeleccionado(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon!=-1){
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            var nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            var apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            var membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.idLabel.setText("ID: "+id);
            this.nombreTexto.setText(nombre);
            this.apellidoTexto.setText(apellido);
            this.membresiaTexto.setText(membresia);

        }
    }

    private void eliminarCliente(){
        var renglon= clientesTabla.getSelectedRow();
        if(renglon!=-1){
            var idClienteStr = clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente = Integer.parseInt(idClienteStr);
            var cliente = new Cliente();
            cliente.setId(this.idCliente);
            clienteServicio.elminiarCliente(cliente);
            mostrarMensaje("Se eliminó al cliente con id: " + this.idCliente + " exitosamente");
            limpiarFormulario();
            listarClientes();
        }
    }

    private void limpiarFormulario(){
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        this.idCliente=null;
        idLabel.setText("");
        this.clientesTabla.getSelectionModel().clearSelection();

    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
}


