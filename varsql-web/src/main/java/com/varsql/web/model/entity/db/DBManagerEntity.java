package com.varsql.web.model.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.varsql.web.model.base.AbstractRegAuditorModel;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.model.id.DBManagerId;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@IdClass(DBManagerId.class)
@Table(name = DBManagerEntity._TB_NAME)
public class DBManagerEntity extends AbstractRegAuditorModel{
	public final static String _TB_NAME="VTDATABASE_MANAGER";

	@Id
	@Column(name =DBConnectionEntity.VCONNID)
	private String vconnid;

	@Id
	@Column(name =UserEntity.VIEWID)
	private String viewid;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = UserEntity.VIEWID, nullable = false, insertable =false , updatable =false)
	private UserEntity userModel;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = DBConnectionEntity.VCONNID, nullable = false , insertable =false , updatable =false)
	private DBConnectionEntity dbConnectionModel;

	@Builder
	public DBManagerEntity(String vconnid, String viewid) {
		this.vconnid = vconnid;
		this.viewid = viewid;

	}
	public final static String VCONNID="vconnid";

	public final static String VIEWID="viewid";
}