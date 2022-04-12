#!/bin/sh

if [ "${PWD##*/}" = "lib" ];
then
  cd ..
fi
mvn dependency:copy-dependencies
# Adresář lib
# obsahuje pouze skript s názvem get-libs.sh,
# který po spuštění stáhne z internetu požadované externí knihovny či soubory
# (předpokládejte prostředí Unix/Linux a přítomnost nástroje wget a zip)
# externí knihovny lze stáhnout přímo ze zdroje, další soubory můžete dočasně umístit na váš webový prostor
# pokud vaše řešení žádné další knihovny a soubory nepožaduje, nedělá skript nic
