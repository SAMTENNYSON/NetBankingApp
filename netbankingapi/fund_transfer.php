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
$from_cif = $data->from_cif;
$to_cif = $data->to_cif;
$amount = $data->amount;
$transaction_pin = $data->transaction_pin;

// Verify TPIN
$result = $conn->query("SELECT balance, transaction_pin FROM users WHERE cif_number='$from_cif'");
if (!$result || $result->num_rows == 0) {
    echo json_encode(["success" => false, "message" => "Sender not found"]);
    exit;
}
$row = $result->fetch_assoc();
if ($row['transaction_pin'] != $transaction_pin) {
    echo json_encode(["success" => false, "message" => "Invalid Transaction PIN"]);
    exit;
}

// Deduct from sender
$conn->query("UPDATE users SET balance = balance - $amount WHERE cif_number='$from_cif'");
// Add to receiver
$conn->query("UPDATE users SET balance = balance + $amount WHERE cif_number='$to_cif'");

// Insert into transactions with category
$conn->query("INSERT INTO transactions (cif_number, type, amount, category) VALUES ('$from_cif','debit',$amount,'Money Transfer')");
$conn->query("INSERT INTO transactions (cif_number, type, amount, category) VALUES ('$to_cif','credit',$amount,'Money Received')");

echo json_encode(["success" => true, "message" => "Transfer successful"]);

$conn->close();
?>
