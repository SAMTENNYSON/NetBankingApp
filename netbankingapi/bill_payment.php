<?php
header("Content-Type: application/json");

$host = "localhost";
$db_name = "netbanking";
$db_user = "root";
$db_pass = "";

$conn = new mysqli($host, $db_user, $db_pass, $db_name);

if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "DB connection failed"]);
    exit;
}

$data = json_decode(file_get_contents("php://input"));

$cif_number = $conn->real_escape_string($data->cif_number ?? "");
$bill_type  = $conn->real_escape_string($data->bill_type ?? "");
$acc_no     = $conn->real_escape_string($data->account_number ?? "");
$amount     = floatval($data->amount ?? 0);
$tpin       = $conn->real_escape_string($data->transaction_pin ?? "");

// Validate TPIN
$res = $conn->query("SELECT balance, transaction_pin FROM users WHERE cif_number='$cif_number'");
if ($res && $row = $res->fetch_assoc()) {
    if ($row['transaction_pin'] !== $tpin) {
        echo json_encode(["success" => false, "message" => "Invalid TPIN"]);
        exit;
    }
    if ($row['balance'] < $amount) {
        echo json_encode(["success" => false, "message" => "Insufficient balance"]);
        exit;
    }

    // Deduct balance
    $new_balance = $row['balance'] - $amount;
    $conn->query("UPDATE users SET balance='$new_balance' WHERE cif_number='$cif_number'");

    // Insert payment record
    $conn->query("INSERT INTO bill_payments (cif_number, bill_type, account_number, amount)
                  VALUES ('$cif_number', '$bill_type', '$acc_no', '$amount')");

    echo json_encode(["success" => true, "message" => "Bill payment successful", "new_balance" => $new_balance]);
} else {
    echo json_encode(["success" => false, "message" => "User not found"]);
}

$conn->close();
?>
