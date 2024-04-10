package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * フォロワーデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_FLW)

@Getter // 全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter // 全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor // 引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor // 全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Follower {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.FLW_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * フォロワーを登録した従業員
     */
    @JoinColumn(name = JpaConst.FLW_COL_USR, nullable = false)
    private Employee employee;

    /**
     * フォローされた従業員
     */
    @JoinColumn(name = JpaConst.FLW_COL_FLW, nullable = false)
    private Report follower;

    /**
     * 登録日時
     */
    @Column(name = JpaConst.FLW_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.FLW_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;
}
