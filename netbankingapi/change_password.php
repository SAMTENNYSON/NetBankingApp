<?php
$host = "localhost";
$db_name = "netbanking";
$db_user = "root";
$db_pass = "";

$conn = new mysqli($host, $db_user, $db_pass, $db_name);

if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "DB connection failed"]));
}

$data = json_decode(file_get_contents("php://input"));
$cif_number = $data->cif_number;
$old_password = $data->old_password;
$new_password = $data->new_password;

// Verify old password
$result = $conn->query("SELECT password FROM users WHERE cif_number='$cif_number'");
if (!$result || $result->num_rows == 0) {
    echo json_encode(["success" => false, "message" => "User not found"]);
    exit;
}

$row = $result->fetch_assoc();
if ($row['password'] != $old_password) {
    echo json_encode(["success" => false, "message" => "Old password incorrect"]);
    exit;
}

// Update new password
$conn->query("UPDATE users SET password='$new_password' WHERE cif_number='$cif_number'");
echo json_encode(["success" => true, "message" => "Password changed successfully"]);

$conn->close();
?>
