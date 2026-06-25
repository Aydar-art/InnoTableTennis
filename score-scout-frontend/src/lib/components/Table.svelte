<script lang="ts">
  export let columns: string[];
  export let rows: string[][];
</script>

<div class="pt-5">
  <div class="text-2xl"><b>Rankings</b></div>
  <table>
    <thead>
      <tr>
        {#each columns as column}
          <th>{column}</th>
        {/each}
      </tr>
    </thead>
    <tbody>
      {#each rows as row}
        <tr>
          {#each row as cell}
            <td>{cell}</td>
          {/each}
        </tr>
      {/each}
    </tbody>
  </table>
</div>

<style>
  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 1rem;
    font-size: 20px;
  }

  th {
    text-align: left;
    padding: 8px;
    background-color: #f8d7da;
  }

  td {
    padding: 4px;
  }

  table th {
    background-color: white;
    border-bottom: 3px solid rgba(246, 218, 218, 1);
    color: #c21807;
  }

  table td {
    color: #3a1f1b;
  }
</style>
