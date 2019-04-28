package model;

/**
 * ADMIN : can create update delete users (included moderators)
 * MODERATOR : can create update delete users (only clients)
 * CLIENT : can see only rss feed
 */
public enum UserRoleType {

    ADMIN,
    MODERATOR,
    CLIENT
}
