package com.dpagliotto.paykids.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "titular")
public class Titular extends BaseModel implements Parcelable {

    @DatabaseField(columnName = "id", id = true)
    private Integer id;

    @DatabaseField(columnName = "nome")
    private String nome;

    @DatabaseField(columnName = "sobrenome")
    private String sobrenome;

    @DatabaseField(columnName = "cpf")
    private String cpf;

    @DatabaseField(columnName = "telefone")
    private String telefone;

    @DatabaseField(columnName = "email")
    private String email;

    @DatabaseField(columnName = "idzoop")
    private String idzoop;

    @DatabaseField(columnName = "cartao_id", foreign = true, foreignColumnName = "id")
    private Cartao cartao;

    @ForeignCollectionField()
    private ForeignCollection<Dependente> dependentes;

    public Titular() {

    }

    protected Titular(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        nome = in.readString();
        sobrenome = in.readString();
        cpf = in.readString();
        telefone = in.readString();
        email = in.readString();
        idzoop = in.readString();
        cartao = in.readParcelable(Cartao.class.getClassLoader());
    }

    public static final Creator<Titular> CREATOR = new Creator<Titular>() {
        @Override
        public Titular createFromParcel(Parcel in) {
            return new Titular(in);
        }

        @Override
        public Titular[] newArray(int size) {
            return new Titular[size];
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
        parcel.writeString(cpf);
        parcel.writeString(telefone);
        parcel.writeString(email);
        parcel.writeString(idzoop);
        parcel.writeParcelable(cartao, i);
    }

    public Integer getId() {
        return id;
    }

    public Titular setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Titular setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public Titular setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public Titular setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public String getTelefone() {
        return telefone;
    }

    public Titular setTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Titular setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getIdzoop() {
        return idzoop;
    }

    public Titular setIdzoop(String idzoop) {
        this.idzoop = idzoop;
        return this;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public Titular setCartao(Cartao cartao) {
        this.cartao = cartao;
        return this;
    }

    public ForeignCollection<Dependente> getDependentes() {
        return dependentes;
    }

    public Titular setDependentes(ForeignCollection<Dependente> dependentes) {
        this.dependentes = dependentes;
        return this;
    }
}