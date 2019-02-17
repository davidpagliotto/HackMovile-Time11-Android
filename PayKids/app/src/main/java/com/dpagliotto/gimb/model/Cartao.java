package com.dpagliotto.gimb.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "cartao")
public class Cartao extends BaseModel implements Parcelable {

    @DatabaseField(columnName = "id", id = true)
    private Integer id;

    @DatabaseField(columnName = "tokenzoop")
    private String tokenzoop;

    @DatabaseField(columnName = "idzoop")
    private String idzoop;

    @DatabaseField(columnName = "numero")
    private String numero;

    @DatabaseField(columnName = "titular")
    private String titular;

    @DatabaseField(columnName = "anoVencimento")
    private String anoVencimento;

    @DatabaseField(columnName = "mesVencimento")
    private String mesVencimento;

    @DatabaseField(columnName = "ccv")
    private String ccv;

    public Cartao() {

    }

    protected Cartao(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        tokenzoop = in.readString();
        idzoop = in.readString();
        numero = in.readString();
        titular = in.readString();
        anoVencimento = in.readString();
        mesVencimento = in.readString();
        ccv = in.readString();
    }

    public static final Creator<Cartao> CREATOR = new Creator<Cartao>() {
        @Override
        public Cartao createFromParcel(Parcel in) {
            return new Cartao(in);
        }

        @Override
        public Cartao[] newArray(int size) {
            return new Cartao[size];
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
        parcel.writeString(tokenzoop);
        parcel.writeString(idzoop);
        parcel.writeString(numero);
        parcel.writeString(titular);
        parcel.writeString(anoVencimento);
        parcel.writeString(mesVencimento);
        parcel.writeString(ccv);
    }

    public Integer getId() {
        return id;
    }

    public Cartao setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTokenzoop() {
        return tokenzoop;
    }

    public Cartao setTokenzoop(String tokenzoop) {
        this.tokenzoop = tokenzoop;
        return this;
    }

    public String getIdzoop() {
        return idzoop;
    }

    public Cartao setIdzoop(String idzoop) {
        this.idzoop = idzoop;
        return this;
    }

    public String getNumero() {
        return numero;
    }

    public Cartao setNumero(String numero) {
        this.numero = numero;
        return this;
    }

    public String getTitular() {
        return titular;
    }

    public Cartao setTitular(String titular) {
        this.titular = titular;
        return this;
    }

    public String getAnoVencimento() {
        return anoVencimento;
    }

    public Cartao setAnoVencimento(String anoVencimento) {
        this.anoVencimento = anoVencimento;
        return this;
    }

    public String getMesVencimento() {
        return mesVencimento;
    }

    public Cartao setMesVencimento(String mesVencimento) {
        this.mesVencimento = mesVencimento;
        return this;
    }

    public String getCcv() {
        return ccv;
    }

    public Cartao setCcv(String ccv) {
        this.ccv = ccv;
        return this;
    }
}