import { Button } from "../../../../shared/components/Button/Button";
import { Input } from "../../../../shared/components/Input/Input";
import styles from "./SearchBar.module.css";
import { SearchButton } from "./SearchButton/SearchButton";
export const SearchBar = ({
  handleSearch,
  handleToggleOptions,
  searchValue,
  setSearchValue,
}: {
  handleSearch: () => void;
  handleToggleOptions: () => void;
  searchValue: string;
  setSearchValue(value: string): void;
}) => {
  return (
    <div className={styles["search-bar"]}>
      <Input
        placeholder="Search"
        type="text"
        onChange={(e) => setSearchValue(e.target.value)}
        value={searchValue}
        className={styles["search-bar__input"]}
      />
      <SearchButton
        searchValueEmpty={searchValue.trim() === ""}
        onClick={handleSearch}
      />
      <Button
        className={styles["search-options-button"]}
        onClick={handleToggleOptions}
        iconPath="/tune.svg"
      />
    </div>
  );
};
