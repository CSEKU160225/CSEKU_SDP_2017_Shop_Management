<?php
require"init.php";

$product_name=$_POST["pnamed"];
$quantity=$_POST["quantity"];
$price=$_POST["price"];
$email=$_POST["email"];

//$type=$_POST["type"];
//$pname=$_POST["pname"];

// $product_name="Pencil Battery";
// $quantity="2";
// $price="6";
// $email="Mehadi";

$sql="SELECT * from registration where Email like '$email'";
$result=mysqli_query($con,$sql);
$row=mysqli_fetch_assoc($result);
$person_id=$row['res_id'];

echo $person_id;

$sql_qury2="SELECT * FROM shop WHERE res_id like '$person_id'";
$result2=mysqli_query($con,$sql_qury2);
if($row2=mysqli_fetch_assoc($result2))
	$id=$row2['shop_id'];

echo $id;

$sql_query3="INSERT INTO sell (product_name, sell_quantity, total_sell_price, sell_date,shop_id) VALUES ('$product_name', '$quantity', '$price', CURRENT_TIMESTAMP,'$id')";
if(mysqli_query($con,$sql_query3))
{
	$sql_qury2="SELECT quantity FROM product WHERE product_name like '$product_name' and shop_id like '$id'";
	$result2=mysqli_query($con,$sql_qury2);
	if($row2=mysqli_fetch_assoc($result2))
	$id2=$row2['quantity'];
	$new_id=$id2-$quantity;

	$sql_query3="UPDATE product SET quantity = '$new_id' where shop_id like '$id' and product_name like '$product_name'";
	if($con->query($sql_query3)===TRUE)
	{
	echo"Data insertion success...";
	}
}


// $reserve_quantity=-1;
// $total=0;

// $sql_qury5="SELECT * FROM sell WHERE product_name like '$product_name' order by sell_date desc";
// $result5=mysqli_query($con,$sql_qury5);
// if($row2=mysqli_fetch_assoc($result5))
// $reserve_quantity=$row2['reserve_quantity'];

// if($reserve_quantity==-1)
// {

// $sql_qury4="SELECT * FROM product WHERE product_name like '$product_name'";
// $result4=mysqli_query($con,$sql_qury4);
// if($row2=mysqli_fetch_assoc($result4))
// {
// 	$reserve_quantity=$row2['quantity'];
// 	$total=$row2['total_sell_price'];
// }
    
// }
// $total=$total+$quantity*$price;
// $reserve_quantity=$reserve_quantity-$quantity;
// $sql_query3="INSERT INTO `sell` (`sell_id`, `product_name`, `sell_quantity`, `reserve_quantity`, `total_sell_price`, `sell_date`) VALUES (NULL, '$product_name', '$quantity', '$reserve_quantity', '$total', CURRENT_TIMESTAMP);";
// if(mysqli_query($con,$sql_query3))
// {
// echo"Data insertion success...";
// }
?>