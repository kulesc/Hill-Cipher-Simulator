# Examples

In this directory you can find examples of two test files in `json` format. These are used as input for simulator in testing mode. The idea is that a professor can give a plaintext / ciphertext and a key / inverse key and the student calculates the ciphertext / plaintext by hand. The student then enters the result in simulator which automatically checks if answer is correct or not and provides a step by step solution.

Format of encryption test:

```json
{
    "test": "encryption",
    "plaintext": "<plaintext>",
    "key_size": "<key_size>",
    "fill": "<fill_character>",
    "key": {
        "<row_number>": "<space_separated_row_values>",
        "<row_number>": "<space_separated_row_values>",
        ...
    }
}
```

Format of decryption test:

```json
{
    "test": "decryption",
    "ciphertext": "<ciphertext>",
    "key_size": "<key_size>",
    "inverse_key": {
        "<row_number>": "<space_separated_row_values>",
        "<row_number>": "<space_separated_row_values>",
        ...
    }
}
```
