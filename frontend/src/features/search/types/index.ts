export enum SearchType {
  TITLE = "title",
  CONTENT = "content",
}

export enum Direction {
  ASC = "asc",
  DESC = "desc",
}

export enum SortBy {
  TITLE = "title",
  CREATED_AT = "createdAt",
  UPDATED_AT = "updatedAt",
}
export interface PaginationOptions {
  page: number;
  size: number;
  direction: Direction;
  sortBy: SortBy;
}
