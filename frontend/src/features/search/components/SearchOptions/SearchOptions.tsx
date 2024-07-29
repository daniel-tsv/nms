import { Dropdown } from "../../../../shared/components/Dropdown/Dropdown";
import { Input } from "../../../../shared/components/Input/Input";
import { ToggleButton } from "../../../../shared/components/ToggleButton/ToggleButton";
import { Direction, PaginationOptions, SearchType, SortBy } from "../../types";
import styles from "./SearchOptions.module.css";

interface SearchOptionsProps {
  searchOptionsClosed: boolean;
  searchType: SearchType;
  setSearchType: (type: SearchType) => void;
  pagination: PaginationOptions;
  setPagination: (pagination: PaginationOptions) => void;
}

export const SearchOptions = ({
  searchOptionsClosed,
  searchType,
  setSearchType,
  pagination,
  setPagination,
}: SearchOptionsProps) => {
  const handleSetNotesPerPage = (size: number) => {
    setPagination({ ...pagination, size });
  };

  return (
    searchOptionsClosed || (
      <div className={styles["search-options"]}>
        <div className={styles["option"]}>
          <p>Search in </p>
          <ToggleButton
            options={Object.values(SearchType)}
            selectedOption={searchType}
            setSelectedOption={(type: string) =>
              setSearchType(type as SearchType)
            }
          />
        </div>
        <div className={styles["option"]}>
          <p>Notes per page </p>
          <Input
            className={styles["input"]}
            type="number"
            value={pagination.size}
            onChange={(e) => {
              const value = Number(e.target.value);
              if (value > 0) {
                handleSetNotesPerPage(value);
              }
            }}
          />
        </div>
        <div className={styles["option"]}>
          <p>Order</p>
          <ToggleButton
            selectedOption={pagination.direction}
            setSelectedOption={(dir: string) =>
              setPagination({ ...pagination, direction: dir as Direction })
            }
            options={Object.values(Direction) as string[]}
          />
        </div>
        <div className={styles["option"]}>
          <p>Sort by</p>
          <Dropdown
            options={Object.values(SortBy) as SortBy[]}
            selectedOption={pagination.sortBy}
            setSelectedOption={(sortBy: SortBy) =>
              setPagination({ ...pagination, sortBy })
            }
          />
        </div>
      </div>
    )
  );
};
