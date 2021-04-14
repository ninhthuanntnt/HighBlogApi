package com.high.highblog.model.entity;

import com.high.highblog.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hb_post_statistics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostStatistic
        extends AbstractAuditingColumns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder.Default
    @Column(name = "number_of_votes", nullable = false)
    private Long numberOfVotes = 0L;

    public void updateNumberOfVotes(final VoteType currentVoteType, final VoteType newVoteType) {
        if (currentVoteType == VoteType.UP && newVoteType == VoteType.DOWN) {

            numberOfVotes -= 2;

        } else if (currentVoteType == VoteType.DOWN && newVoteType == VoteType.UP) {

            numberOfVotes += 2;

        } else if (currentVoteType == null && newVoteType == VoteType.UP
                || (currentVoteType == VoteType.DOWN && newVoteType == null)) {

            numberOfVotes += 1;

        } else if ((currentVoteType == null && newVoteType == VoteType.DOWN)
                || (currentVoteType == VoteType.UP && newVoteType == null)) {

            numberOfVotes -= 1;

        }
    }
}
