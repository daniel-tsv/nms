import styles from "./Dropdown.module.css";

interface DropdownProps<T> {
  options: T[];
  selectedOption: T;
  setSelectedOption: (option: T) => void;
}

export const Dropdown = <T extends string>({
  options,
  selectedOption,
  setSelectedOption,
}: DropdownProps<T>) => {
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedOption(event.target.value as T);
  };
  return (
    <select
      className={styles.dropdown}
      value={selectedOption}
      onChange={handleChange}
    >
      {options.map((option: string) => (
        <option key={option} value={option}>
          {option}
        </option>
      ))}
    </select>
  );
};
