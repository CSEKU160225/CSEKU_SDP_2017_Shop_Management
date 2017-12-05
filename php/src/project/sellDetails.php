<?php
require"init.php";

//$item=$_POST["pnamed"];
$item="Pencil Battery";
$response=array();

$total=0;
$price=0;

$sql_qury5="SELECT * FROM sell WHERE product_name like '$item' order by  CURDATE()";
$result5=mysqli_query($con,$sql_qury5);
while($row2=mysqli_fetch_assoc($result5))
{
    $quantity=$row2['sell_quantity'];
    $total=$total+$quantity;
    $sellPrice=$row2['total_sell_price'];
    $price=$price+$sellPrice;
}

array_push($response, array("quantity"=>$total,"price"=>$price));
echo json_encode(array("Server_response"=>$response));
mysqli_close($con);
    
?>