/*
 * Table cartao
 */
CREATE TABLE IF NOT EXISTS cartao (
    id int not null,
    tokenzoop text null,
    idzoop text null,
    numero text null,
    titular text null,
    anoVencimento text null,
    mesVencimento text null,
    ccv text null,
  PRIMARY KEY (id)
);


/*
 * Table titular
 */
CREATE TABLE IF NOT EXISTS titular (
  id int not null,
  nome text null,
  sobrenome text null,
  cpf text null,
  telefone text null,
  email text null,
  idzoop text null,
  cartao_id int null,
  PRIMARY KEY (id)
);


/*
 * Table dependente
 */
CREATE TABLE IF NOT EXISTS dependente (
  id int not null,
  titular_id int not null,
  nome text null,
  sobrenome text null,
  telefone text null,
  email text null,
  idzoop text null,
  saldo numeric null,
  PRIMARY KEY (id)
);


