package com.dpagliotto.paykids.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by davidpagliotto on 27/07/17.
 */

@DatabaseTable(tableName = "dependente")
public class Dependente extends BaseModel implements Parcelable {

    @DatabaseField(columnName = "id", id = true)
    private Integer id;

    @DatabaseField(columnName = "nome")
    private String nome;

    @DatabaseField(columnName = "sobrenome")
    private String sobrenome;

    @DatabaseField(columnName = "telefone")
    private String telefone;

    @DatabaseField(columnName = "email")
    private String email;

    @DatabaseField(columnName = "idzoop")
    private String idzoop;

    @DatabaseField(columnName = "saldo")
    private Double saldo;

    @DatabaseField(columnName = "titular_id", foreign = true)
    private Titular titular;

    public Dependente() {

    }

    protected Dependente(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        nome = in.readString();
        sobrenome = in.readString();
        telefone = in.readString();
        email = in.readString();
        idzoop = in.readString();
        saldo = in.readDouble();
        titular = in.readParcelable(Titular.class.getClassLoader()  );
    }

    public static final Creator<Dependente> CREATOR = new Creator<Dependente>() {
        @Override
        public Dependente createFromParcel(Parcel in) {
            return new Dependente(in);
        }

        @Override
        public Dependente[] newArray(int size) {
            return new Dependente[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(nome);
        parcel.writeString(sobrenome);
        parcel.writeString(telefone);
        parcel.writeString(email);
        parcel.writeString(idzoop);
        parcel.writeDouble(saldo);
        parcel.writeParcelable(titular, i);
    }

    public Integer getId() {
        return id;
    }

    public Dependente setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Dependente setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public Dependente setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
        return this;
    }

    public String getTelefone() {
        return telefone;
    }

    public Dependente setTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Dependente setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getIdzoop() {
        return idzoop;
    }

    public Dependente setIdzoop(String idzoop) {
        this.idzoop = idzoop;
        return this;
    }

    public Double getSaldo() {
        return saldo;
    }

    public Dependente setSaldo(Double saldo) {
        this.saldo = saldo;
        return this;
    }

    public Titular getTitular() {
        return titular;
    }

    public Dependente setTitular(Titular titular) {
        this.titular = titular;
        return this;
    }
}