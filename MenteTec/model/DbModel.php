<?php 

class DbModel{
    private $usuario = 'root';
    private $pass = '';
    private $host = '127.0.0.1';
    private $db = 'dboctavob';
    private $conexion;
    
    public function __construct()
    {
        try {
            $this->conexion = new mysqli($this->host, $this->usuario, $this->pass, $this->db);
            echo ":)";
        } catch(Exception $e) {
            echo ":(";
        }
    }
    
    function insert($REQ)
    {
        try {
            $SQL = "INSERT INTO informacion(nombre, edad)
            VALUES('".$REQ['nombre']."','".$REQ['edad']."')";
            $this->conexion->query($SQL);//Ejecuta el insert
        } catch(Exception $e) {
            echo "error al insertar";
        }
    }
    
    function delete($id)
    {
        try {
            $SQL = "DELETE FROM informacion where id = '$id'";
            $this->conexion->query($SQL);
        } catch(Exception $e) {
             echo "$e";
        }
    }
    
    function update($id, $nombre, $edad)
    {
        try {
            $SQL = "UPDATE informacion SET nombre = '$nombre', edad = '$edad' WHERE id = '$id'";
            
            // Depuración: Mostrar la consulta SQL
            echo "SQL de actualización: $SQL";

            $this->conexion->query($SQL);
        } catch(Exception $e) {
            echo "Error al actualizar: " . $e->getMessage();
        }
    }
  
  
    
    function select()
    {
        try {
            $SQL = "SELECT * FROM informacion";
            $datos = $this->conexion->query($SQL);
            return $datos;
        } catch(Exception $e) {
            echo "HOLAAAAAAAAAAAA";
        }
    }


    function getUsuario($id)
    {
        try {
            $SQL = "SELECT * FROM informacion WHERE id = '$id'";
            $result = $this->conexion->query($SQL);
            if ($result->num_rows > 0) {
                return $result->fetch_assoc();
            } else {
                return null;
            }
        } catch(Exception $e) {
            echo "$e";
        }
    }
}


