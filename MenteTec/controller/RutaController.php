<?php 
include_once('../model/DbModel.php');

class ClassController{

    private $objmodel;


    function __construct()
    {
        $this->objmodel = new DbModel();
    }

    function index(){
        header("Location:../View/index.php");
    }

    function altaView(){
        header("Location:../View/alta.php"); 
    }

    function Autentifica($REQ){
        if($REQ['user'] == 'Admin' && $REQ['pass'] == '123') {
            $cad = "Location:../View/index.php?user=".$REQ['user'];
            header($cad);
        } else {
            header("Location:../index.html");
        } 
    }

    function CierreSesion(){
        session_destroy();
        header("Location:../index.html");
    }

    function Insert($REQ){
        $this->objmodel->insert($REQ);
        $this->Select();
    }

    function Select(){

        $datos = $this->objmodel->select();
        include("../View/consulta.php");
    }

    function Delete($id){
        $this->objmodel->delete($id);
        $this->Select();
    }

    function Editar($id){
        $usuario = $this->objmodel->getUsuario($id);
        include("../View/editar.php");
    }

    function Actualizar($REQ){

        $id = $REQ['id'];
        $nombre = $REQ['nombre'];
        $edad = $REQ['edad'];

        // Depuración: Mostrar los datos recibidos
        echo "ID: $id, Nombre: $nombre, Edad: $edad";

        // Llamada al método update de DbModel
        $this->objmodel->update($id, $nombre, $edad);
        // Redirección
        header("Location: RutaController.php?ruta=7");
    }
}

$objController = new ClassController();

switch($_REQUEST["ruta"]) {
    case 1:
        $objController->index();
        break;
    case 3:
        $objController->altaView(); 
        break;
    case 4:
        $objController->Autentifica($_REQUEST);
        break;   
    case 5:
        $objController->CierreSesion();
        break;    
    case 6:
        $objController->Insert($_REQUEST);
        break;
    case 7:
        $objController->Select();
        break;
    case 8:
        $objController->Delete($_REQUEST['variableid']);
        break;
    case 10:
        $objController->Editar($_REQUEST['id']);
        break;
    case 11:
        $objController->Actualizar($_REQUEST);
        break;
}

?>
