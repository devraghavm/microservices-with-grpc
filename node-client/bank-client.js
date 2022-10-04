const grpc = require('@grpc/grpc-js');
const protoloader = require('@grpc/proto-loader');

const packageDef = protoloader.loadSync('proto/bank-service.proto');
const protoDesc = grpc.loadPackageDefinition(packageDef);

const client = new protoDesc.BankService(
  'localhost:6565',
  grpc.credentials.createInsecure()
);

client.getBalance({ accountNumber: 4 }, (err, balance) => {
  if (err) {
    console.error('Something bad happened');
  } else {
    console.log(`Received: ${balance.amount}`);
  }
});
