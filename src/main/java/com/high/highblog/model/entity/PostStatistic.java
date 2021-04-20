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
import javax.validation.constraints.NotNull;

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

    @NotNull
    @Column(name = "post_id")
    private Long postId;

    @Builder.Default
    @NotNull
    @Column(name = "number_of_votes")
    private Long numberOfVotes = 0L;

    @Builder.Default
    @NotNull
    @Column(name = "number_of_comments")
    private Long numberOfComments = 0L;

    @Builder.Default
    @NotNull
    @Column(name = "number_of_favorites")
    private Long numberOfFavorites = 0L;

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
