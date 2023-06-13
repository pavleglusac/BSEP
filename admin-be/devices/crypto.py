import jks
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.primitives import hashes
import base64
from cryptography.hazmat.primitives.serialization import pkcs12


def sign_message(message):
    my_private_key = load_private_key_from_jks('./keystores/admin.jks', 'admin', 'bebenem')
    signature = sign_string(my_private_key, message)
    return signature


def load_private_key_from_jks(jks_path, jks_password, alias):
    keystore = jks.KeyStore.load(jks_path, jks_password)
    private_key_entry = keystore.private_keys[alias]
    # call decrypt 
    private_key_entry.decrypt("")
    private_key_pkcs8 = private_key_entry.pkey
    private_key = serialization.load_der_private_key(
        private_key_pkcs8,
        password=None,
    )
    return private_key


def sign_string(private_key, message):
    signature = private_key.sign(
        message,
        padding.PKCS1v15(),
        hashes.SHA1()
    )
    signature_b64 = base64.b64encode(signature)
    return str(signature_b64, encoding='utf-8')


def verify_signature(public_key, message, signature_b64):
    signature = base64.b64decode(signature_b64)

    try:
        public_key.verify(
            signature,
            message,
            padding.PKCS1v15(),
            hashes.SHA1()
        )
        return "The signature is valid."
    except Exception as e:
        return "The signature is not valid."



if __name__ == "__main__":
    # s = "Hello world"
    # bs = s.encode()
    # sign_message(bs)



    my_private_key = load_private_key_from_jks('./keystores/admin.jks', 'admin', 'bebenem')

    public_key = my_private_key.public_key()
    message_to_sign = "d2348a79-dbf6-40ec-9ed9-f5d85059c7ee,INFO,Measured temperature,20.6,2023-06-12T23:01:33.745747"
    hash_base_64_signature = b"EtM/WPChjuib3VdFs44CA6Lb7iOIV75EgSG1IZf/UqunfIyDq2fIYsMutOsc/12Enz1BN7hxtE92kV54xtzbG1xLm9o1nfPWMWbfiZVLhG10DzuuAiVPMkp8h/38iKeU/MNZZZaXcxnuCiczeU7JBPk/E/FQ9dCNjwn7cRkM1bP4j9QFFVBV0doZHzJtqA7X0Ap88YpRnrbxYJiW+6EZIOGdJEA2BsG7ur3MMh/263neOjy8nLvu6dJP4H7Pmx+kZ/nTUcQsXubt93S7TqWiZt7I2DdmHWm8TODTloTL3la/z/KhHkIcvecwIUUnv68FzE9frTu8iSBhAmkMAHcDgg=="

    bytes = message_to_sign.encode()
    signature = sign_string(my_private_key, bytes)
    print(signature)

    # # print both public and private keys in PEM format
    # print(public_key.public_bytes(
    #     encoding=serialization.Encoding.PEM,
    #     format=serialization.PublicFormat.SubjectPublicKeyInfo
    # ))
    # print()
    # print(my_private_key.private_bytes(
    #     encoding=serialization.Encoding.PEM,
    #     format=serialization.PrivateFormat.PKCS8,
    #     encryption_algorithm=serialization.NoEncryption()
    # ))


    # print(sign_message(message_to_sign))

    decoded_signature = base64.b64decode(hash_base_64_signature)

    # verify
    print(verify_signature(public_key, message_to_sign.encode(), hash_base_64_signature))