package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * フォローデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_FOL)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_FOL_COUNT_FOLLOWING,
            query = JpaConst.Q_FOL_COUNT_FOLLOWING_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_COUNT_FOLLOWER,
            query = JpaConst.Q_FOL_COUNT_FOLLOWER_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_ALL_FOLLOWING,
            query = JpaConst.Q_FOL_GET_ALL_FOLLOWING_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_ALL_FOLLOWER,
            query = JpaConst.Q_FOL_GET_ALL_FOLLOWER_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_FOLLOWER,
            query = JpaConst.Q_FOL_GET_FOLLOWER_DEF)
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Follow{

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.FOL_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * following
     *
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FOL_COL_FOLLOWING_ID, nullable = false)
    private Employee following;


    /**
     * follower
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FOL_COL_FOLLOWER_ID, nullable = false)
    private Employee follower;
}

