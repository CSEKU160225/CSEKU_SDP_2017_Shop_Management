<?php
require "init.php";
$Email=$_POST["Email"];
$password=$_POST["Password"];

//$Email="Mehadi";
//$password="88";
$sql_query="select * from registration where Email like '$Email' and password like '$password'";
$result=mysqli_query($con,$sql_query);
if($row= mysqli_fetch_assoc($result))
{
$name=$row["Email"];
echo "Hellow welcome".$Email;
}

else
{
echo "no info is available";
}

?>