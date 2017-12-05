<?php
require"init.php";

$item=$_POST["pnamed"];
 //$item="Pencil Battery";


$response=array();

$sql_query3="SELECT * from product where product_name like '$item'";
$result3=mysqli_query($con,$sql_query3);
while($row3=mysqli_fetch_assoc($result3))
{
	array_push($response, array("quantity"=>$row3['quantity']));
}
echo json_encode(array("Server_response"=>$response));

mysqli_close($con);
    
?>