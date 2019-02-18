#!/bin/bash -e

cd "$( dirname "${BASH_SOURCE[0]}" )/.."

find . -name "*.crypt" | while read k; do
echo "decrypt: $k"
echo ${PASSWORD_DEXBOARD} | gpg --batch --yes --passphrase-fd 0 --output "$(echo "$k" | sed "s/\.crypt$//g")" --decrypt "$k"
done