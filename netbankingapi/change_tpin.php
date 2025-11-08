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
$old_tpin = $data->old_tpin;
$new_tpin = $data->new_tpin;

// Verify old TPIN
$result = $conn->query("SELECT transaction_pin FROM users WHERE cif_number='$cif_number'");
if (!$result || $result->num_rows == 0) {
    echo json_encode(["success" => false, "message" => "User not found"]);
    exit;
}

$row = $result->fetch_assoc();
if ($row['transaction_pin'] != $old_tpin) {
    echo json_encode(["success" => false, "message" => "Old Transaction PIN incorrect"]);
    exit;
}

// Update new TPIN
$conn->query("UPDATE users SET transaction_pin='$new_tpin' WHERE cif_number='$cif_number'");
echo json_encode(["success" => true, "message" => "Transaction PIN changed successfully"]);

$conn->close();
?>
